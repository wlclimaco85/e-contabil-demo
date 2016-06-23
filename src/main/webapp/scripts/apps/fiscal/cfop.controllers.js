
(function() {
angular.module('wdApp.apps.produtoss', ['datatables','angularModalService', 'datatables.buttons', 'datatables.light-columnfilter', 'datatables.bootstrap','datatables.columnfilter'])
    .controller('RowSelectCtrl', CfopController);

function CfopController($scope, $compile, DTOptionsBuilder, DTColumnBuilder,ModalService) {
    var vm = this;

    vm.message = '';
    vm.edit = edit;
    vm.delete = deleteRow;
    vm.dtInstance = {};
    vm.persons = {};

    vm.dtOptions = DTOptionsBuilder.fromSource('cfop.json')
        .withDOM('frtip')
        .withPaginationType('full_numbers')
        .withOption('createdRow', createdRow)
        .withPaginationType('full_numbers')
        .withColumnFilter({
            aoColumns: [{
                type: 'number'
            }, {
                type: 'number',
            }, {
                type: 'select',
                values: ['Entrada', 'Saida']
            },{
                type: 'text'
            },{
                type: 'text'
            },{
                type: 'text'
            }]
        })
        .withButtons([
            {
                extend: "colvis",
                fileName:  "Data_Analysis",
                exportOptions: {
                    columns: ':visible'
                },
                exportData: {decodeEntities:true}
            },
            {
            extend: "csvHtml5",
                fileName:  "Data_Analysis",
                exportOptions: {
                    columns: ':visible'
                },
                exportData: {decodeEntities:true}
            },
            {
                extend: "pdfHtml5",
                fileName:  "Data_Analysis",
                title:"Data Analysis Report",
                exportOptions: {
                    columns: ':visible'
                },
                exportData: {decodeEntities:true}
            },
            {
                extend: "copy",
                fileName:  "Data_Analysis",
                title:"Data Analysis Report",
                exportOptions: {
                    columns: ':visible'
                },
                exportData: {decodeEntities:true}
            },
            {
                extend: "print",
                //text: 'Print current page',
                autoPrint: true,
                exportOptions: {
                    columns: ':visible'
                }
            },
            {
                extend: "excelHtml5",
                filename:  "Data_Analysis",
                title:"Data Analysis Report",
                exportOptions: {
                    columns: ':visible'
                },
                //CharSet: "utf8",
                exportData: { decodeEntities: true }
            },
            {
                text: 'Novo CFOP',
                key: '1',
                action: function (e, dt, node, config) {
                    ModalService.showModal({
                        templateUrl: 'modal.html',
                        controller: "RowSelectCtrl"
                    }).then(function(modal) {
                        modal.element.modal();
                        modal.close.then(function(result) {
                            $scope.message = "You said " + result;
                        });
                    });
                }
            }
        ])

    vm.dtColumns = [
       DTColumnBuilder.newColumn('id').withTitle('ID'),
        DTColumnBuilder.newColumn('cfop').withTitle('CFOP'),
        DTColumnBuilder.newColumn('natureza').withTitle('Natureza'),
        DTColumnBuilder.newColumn('descricao').withTitle('descricao'),
        DTColumnBuilder.newColumn('simplificado').withTitle('simplificado').notVisible(),
        DTColumnBuilder.newColumn('icms').withTitle('icms').notVisible(),
        DTColumnBuilder.newColumn('icmsReduzido').withTitle('icmsReduzido').notVisible(),
        DTColumnBuilder.newColumn('margemAgregadaST').withTitle('margemAgregadaST').notVisible(),
        DTColumnBuilder.newColumn('cstPrincipal').withTitle('cstPrincipal').notVisible(),
        DTColumnBuilder.newColumn('classFiscal').withTitle('classFiscal').notVisible(),
        DTColumnBuilder.newColumn('observacao').withTitle('observacao').notVisible(),
        DTColumnBuilder.newColumn('modifyUser').withTitle('modifyUser').notVisible(),
        DTColumnBuilder.newColumn('modifyDateUTC').withTitle('modifyDateUTC').notVisible(),
        DTColumnBuilder.newColumn(null).withTitle('Ações').notSortable().renderWith(actionsHtml)
    ];

    function edit(person) {
        ModalService.showModal({
            templateUrl: 'modal.html',
            controller: "RowSelectCtrl"
        }).then(function(modal) {
            modal.element.modal();
            modal.close.then(function(result) {
                $scope.message = "You said " + result;
            });
        });
    }
    function deleteRow(person) {
        ModalService.showModal({
            templateUrl: 'cfopDelete.html',
            controller: "RowSelectCtrl"
        }).then(function(modal) {
            modal.element.modal();
            modal.close.then(function(result) {
                $scope.message = "You said " + result;
            });
        });
    }
    function createdRow(row, data, dataIndex) {
        // Recompiling so we can bind Angular directive to the DT
        $compile(angular.element(row).contents())($scope);
    }
    function actionsHtml(data, type, full, meta) {
        vm.persons[data.id] = data;
        return '<button class="btn btn-warning" ng-click="vm.edit(vm.persons[' + data.id + '])">' +
            '   <i class="fa fa-edit"></i>' +
            '</button>&nbsp;' +
            '<button class="btn btn-danger" ng-click="vm.delete(vm.persons[' + data.id + '])">' +
            '   <i class="fa fa-trash-o"></i>' +
            '</button>';
    }
}
})();