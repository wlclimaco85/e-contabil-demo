(function() {
angular.module('wdApp.apps.produtoss', ['datatables', 'datatables.buttons', 'datatables.light-columnfilter'])
    .controller('CfopController', CfopController);

function CfopController($scope, $compile, DTOptionsBuilder, DTColumnBuilder) {
    var vm = this;

    vm.message = '';
    vm.edit = edit;
    vm.delete = deleteRow;
    vm.dtInstance = {};
    vm.persons = {};

    vm.dtOptions = DTOptionsBuilder.fromSource('data.json')
        .withDOM('frtip')
        .withPaginationType('full_numbers')
        .withOption('createdRow', createdRow)
        .withPaginationType('full_numbers')
        .withLightColumnFilter({
            '0' : {
                type : 'text'
            },
            '1' : {
                type : 'text'
            },
            '2' : {
                type : 'select',
                values: [{
                    value: 'Yoda', label: 'Yoda foobar'
                }, {
                    value: 'Titi', label: 'Titi foobar'
                }, {
                    value: 'Kyle', label: 'Kyle foobar'
                }, {
                    value: 'Bar', label: 'Bar foobar'
                }, {
                    value: 'Whateveryournameis', label: 'Whateveryournameis foobar'
                }]
            }
        })
        // Active Buttons extension
        .withButtons([
            'colvis',
            'copy',
            'print',
            'excel',
            {
                text: 'Novo CFOP',
                key: '1',
                action: function (e, dt, node, config) {
                    alert('Button activated');
                }
            }
        ]);
    vm.dtColumns = [
        DTColumnBuilder.newColumn('id').withTitle('ID'),
        DTColumnBuilder.newColumn('cfop').withTitle('CFOP'),
        DTColumnBuilder.newColumn('natureza').withTitle('Natureza'),
        DTColumnBuilder.newColumn('simplificado').withTitle('Simplificado'),
        DTColumnBuilder.newColumn('classFiscal').withTitle('Classificação Fiscal'),
        DTColumnBuilder.newColumn(null).withTitle('Actions').notSortable().renderWith(actionsHtml)
    ];

    function edit(person) {
        vm.message = 'You are trying to edit the row: ' + JSON.stringify(person);
        // Edit some data and call server to make changes...
        // Then reload the data so that DT is refreshed
        vm.dtInstance.reloadData();
    }
    function deleteRow(person) {
        vm.message = 'You are trying to remove the row: ' + JSON.stringify(person);
        // Delete some data and call server to make changes...
        // Then reload the data so that DT is refreshed
        vm.dtInstance.reloadData();
    }
    function createdRow(row, data, dataIndex) {
        // Recompiling so we can bind Angular directive to the DT
        $compile(angular.element(row).contents())($scope);
    }
    function actionsHtml(data, type, full, meta) {
        vm.persons[data.id] = data;
        return '<button class="btn btn-warning" ng-click="showCase.edit(showCase.persons[' + data.id + '])">' +
            '   <i class="fa fa-edit"></i>' +
            '</button>&nbsp;' +
            '<button class="btn btn-danger" ng-click="showCase.delete(showCase.persons[' + data.id + '])">' +
            '   <i class="fa fa-trash-o"></i>' +
            '</button>';
    }
}
})();