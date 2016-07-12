(function() {
angular.module('wdApp.apps.orcamento', ['datatables','angularModalService', 'datatables.buttons', 'datatables.light-columnfilter', 'datatables.bootstrap','datatables.columnfilter'])
    .controller('OrcamentoController', RowSelect);

function RowSelect($scope, $compile, DTOptionsBuilder, DTColumnBuilder,ModalService) {
    
    var vm = this;
    vm.selected = {};
    vm.selectAll = false;
    vm.toggleAll = toggleAll;
    vm.toggleOne = toggleOne;
    vm.message = '';
    vm.edit = edit;
    vm.delete = deleteRow;
    vm.dtInstance = {};
    vm.persons = {};

    var titleHtml = '<input type="checkbox" ng-model="vm.selectAll"' +
        'ng-click="vm.toggleAll(vm.selectAll, vm.selected)">';

    vm.dtOptions = DTOptionsBuilder.fromSource('orcamento.json')
        .withDOM('frtip')
        .withPaginationType('full_numbers')
        .withOption('createdRow', createdRow)
        .withPaginationType('full_numbers')
        .withColumnFilter({
            aoColumns: [null,{
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
        .withOption('initComplete', function (settings,json) {
            
            $('.dt-buttons').find('.dt-button:eq(1)').before(

            '<select class="form-control col-sm-3 btn btn-primary dropdown-toggle" data-ng-options="t.name for t in vm.types"'+
              'data-ng-model="vm.object.type" style="height: 32px;margin-left: 8px;margin-right: 6px;width: 200px !important; " ng-change="vm.deleteRowAll(vm.selected)">'+
              
                '<option>Ações <span class="badge selected badge-danger main-badge" data-ng-show="{{vm.countSeleted()}}"</span></option>'+
                '<option>Remover Todos <span class="badge selected badge-danger main-badge"  data-ng-show="{{vm.countSeleted()}}"></span></option>'+
               '</select>'

            )
        })
        .withOption('processing', true)
        .withOption('language',{
            paginate : {            // Set up pagination text
                first: "&laquo;",
                last: "&raquo;",
                next: "&rarr;",
                previous: "&larr;"
            },
            lengthMenu: "_MENU_ records per page" 
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
                text: 'Novo Orçãmento',
                key: '1',
                action: function (e, dt, node, config) {
                    ModalService.showModal({
                        templateUrl: 'CFOPmodal.html',
                        controller: "RowSelectCtrl"
                    }).then(function(modal) {

                        modal.element.modal();
                        openDialog();
                        modal.close.then(function(result) {
                            $scope.message = "You said " + result;
                        });
                    });
                }
            }
        ])

      vm.dtColumns = [
        DTColumnBuilder.newColumn(null).withTitle(titleHtml).notSortable()
            .renderWith(function(data, type, full, meta) {
                vm.selected[full.id] = false;
                return '<input type="checkbox" ng-model="showCase.selected[' + data.id + ']" ng-click="showCase.toggleOne(showCase.selected)"/>';
            }),

        DTColumnBuilder.newColumn('id').withTitle('ID'),     
        DTColumnBuilder.newColumn('cliente').withTitle('cliente'),
        DTColumnBuilder.newColumn('data').withTitle('data'),
         DTColumnBuilder.newColumn(null).withTitle('qntProd').notSortable()
            .renderWith(function(data, type, full, meta) {
                return '<a> '+ data.qntProd +'</a>';
            }),
       // DTColumnBuilder.newColumn('qntProd').withTitle('qntProd'),
        DTColumnBuilder.newColumn('valorPedido').withTitle('valorPedido'),
        DTColumnBuilder.newColumn('valorItens').withTitle('valorItens'),
        DTColumnBuilder.newColumn('observacao').withTitle('observacao'),
        DTColumnBuilder.newColumn('modifyUser').withTitle('modifyUser').notVisible(),
        DTColumnBuilder.newColumn('modifyDateUTC').withTitle('modifyDateUTC').notVisible(),
        DTColumnBuilder.newColumn('status').withTitle('status'),
        DTColumnBuilder.newColumn(null).withTitle('Ações').notSortable().renderWith(actionsHtml).withOption('width', '150px')
    ];

    function edit(person) {
        ModalService.showModal({
            templateUrl: 'CFOPmodal.html',
            controller : "RowSelectCtrl"
        }).then(function(modal) {
            modal.element.modal();
            openDialog();
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

    function deleteRowAll(person) {
        debugger
        ModalService.showModal({
            templateUrl: 'cfopAllDelete.html',
            controller: "CfopController"
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

        return ' <div class="dropdown">'+
          '<button class="btn btn-info dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">'+
           ' Açoes'+
            '<span class="caret"></span>'+
          '</button>'+
          '<ul class="dropdown-menu" aria-labelledby="dropdownMenu1">'+
            '<li><a href="#">Deletar</a></li>'+
            '<li><a href="#">Alterar</a></li>'+
            '<li><a href="#">Transformar em Pedido Compra</a></li>'+
            '<li role="separator" class="divider"></li>'+
            '<li><a href="#">Transformar Em NF-e</a></li>'+
          '</ul>'+
        '</div>'
/*

        return '<button class="btn btn-warning" ng-click="vm.edit(vm.persons[' + data.id + '])">' +
            '   <i class="fa fa-edit"></i>' +
            '</button>&nbsp;' +
            '<button class="btn btn-danger" ng-click="vm.delete(vm.persons[' + data.id + '])">' +
            '   <i class="fa fa-trash-o"></i>' +
            '</button>';*/
    }
    function toggleAll (selectAll, selectedItems) {
        for (var id in selectedItems) {
            if (selectedItems.hasOwnProperty(id)) {
                selectedItems[id] = selectAll;
            }
        }
    }
    function toggleOne (selectedItems) {
        for (var id in selectedItems) {
            if (selectedItems.hasOwnProperty(id)) {
                if(!selectedItems[id]) {
                    vm.selectAll = false;
                    return;
                }
            }
        }
        vm.selectAll = true;
    }

    function openDialog() 
    {
        bookIndex = 0;
        $('#pdVendasForm')
        .formValidation({
            framework: 'bootstrap',
            icon: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {

            'book[0].produto': notEmptyStringMinMaxRegexp,
            'book[0].quantidade': integerNotEmptyValidation,
            'book[0].vlUnitario': integerNotEmptyValidation,


        }
        })
        // Add button click handler
        .on('click', '.addButton', function() {
            bookIndex++;
            var $template = $('#bookTemplate'),
                $clone    = $template
                                .clone()
                                .removeClass('hide')
                                .removeAttr('id')
                                .attr('data-book-index', bookIndex)
                                .insertBefore($template);

            // Update the name attributes
            $clone
                .find('[name="produto"]').attr('name', 'book[' + bookIndex + '].produto').end()
                .find('[name="quantidade"]').attr('name', 'book[' + bookIndex + '].quantidade').end()
                .find('[name="vlUnitario"]').attr('name', 'book[' + bookIndex + '].vlUnitario').end()
                .find('[name="desconto"]').attr('name', 'book[' + bookIndex + '].desconto').end();

            // Add new fields
            // Note that we also pass the validator rules for new field as the third parameter
            $('#pdVendasForm')
                .formValidation('addField', 'book[' + bookIndex + '].produto',notEmptyStringMinMaxRegexp)
                .formValidation('addField', 'book[' + bookIndex + '].quantidade',integerNotEmptyValidation)
                .formValidation('addField', 'book[' + bookIndex + '].vlUnitario',integerNotEmptyValidation);
        })// Remove button click handler
        .on('click', '.removeButton', function() {
            var $row  = $(this).parents('.form-group'),
                index = $row.attr('data-book-index');

            // Remove fields
            $('#bookForm')
                .formValidation('removeField', $row.find('[name="book[' + index + '].produto"]'))
                .formValidation('removeField', $row.find('[name="book[' + index + '].quantidade"]'))
                .formValidation('removeField', $row.find('[name="book[' + index + '].vlUnitario"]'))
                .formValidation('removeField', $row.find('[name="book[' + index + '].desconto"]'));

            // Remove element containing the fields
            $row.remove();
        });
        $("select").select2({
          placeholder: "Select a state",
          allowClear: true
        });



    }

}
})();