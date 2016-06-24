(function() {
angular.module('wdApp.apps.nfEntrada', ['datatables','angularModalService', 'datatables.buttons', 'datatables.light-columnfilter', 'datatables.bootstrap','datatables.columnfilter'])
    .controller('NFEntradaController', RowSelect);

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

    vm.dtOptions = DTOptionsBuilder.fromSource('notaEntrada.json')
        .withDOM('frtip')
        .withPaginationType('full_numbers')
        .withOption('createdRow', createdRow)
        .withPaginationType('full_numbers')
        .withColumnFilter({
            aoColumns: [null,{
                type: 'number'
            }, {
                type: 'number'
            }, {
                type: 'text'
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
                text: 'Nova Nota Entrada',
                key: '1',
                action: function (e, dt, node, config) {
                    ModalService.showModal({
            templateUrl: 'modalNfEntrada.html',
            controller: "RowSelectCtrl",
        }).then(function(modal) {
            modal.element.modal();
            bookIndex = 0;
            $('#empresaForm')
                .formValidation({
                    framework: 'bootstrap',
                    icon: {
                        valid: 'glyphicon glyphicon-ok',
                        invalid: 'glyphicon glyphicon-remove',
                        validating: 'glyphicon glyphicon-refresh'
                    },
                    fields: {

                    'book[0].fornecedor': notEmptyStringMinMaxRegexp,
                    'book[0].qtd': notEmptyStringMinMax,
                    'book[0].unit': integerNotEmptyValidation,
                    'financ[0].parcela' : integerNotEmptyValidation,
                    'financ[0].valor' : integerNotEmptyValidation,
                    'financ[0].dtVencimento': integerNotEmptyValidation

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
                        .find('[name="fornecedor"]').attr('name', 'book[' + bookIndex + '].fornecedor').end()
                        .find('[name="qtd"]').attr('name', 'book[' + bookIndex + '].qtd').end()
                        .find('[name="unit"]').attr('name', 'book[' + bookIndex + '].unit').end()
                        .find('[name="frete"]').attr('name', 'book[' + bookIndex + '].frete').end()
                        .find('[name="seg"]').attr('name', 'book[' + bookIndex + '].seg').end()
                        .find('[name="desc"]').attr('name', 'book[' + bookIndex + '].desc').end()
                        .find('[name="ipi"]').attr('name', 'book[' + bookIndex + '].ipi').end()
                        .find('[name="icms"]').attr('name', 'book[' + bookIndex + '].icms').end()
                        .find('[name="trib"]').attr('name', 'book[' + bookIndex + '].trib').end();

                    // Add new fields
                    // Note that we also pass the validator rules for new field as the third parameter
                    $('#empresaForm')
                        .formValidation('addField', 'book[' + bookIndex + '].fornecedor',notEmptyStringMinMaxRegexp)
                        .formValidation('addField', 'book[' + bookIndex + '].qtd',notEmptyStringMinMaxRegexp)
                        .formValidation('addField', 'book[' + bookIndex + '].unit',integerNotEmptyValidation);
                }).on('click', '.addfinanc', function() {
                    bookIndex++;
                    var $template = $('#bookTemplate1'),
                        $clone    = $template
                                        .clone()
                                        .removeClass('hide')
                                        .removeAttr('id')
                                        .attr('data-book-index', bookIndex)
                                        .insertBefore($template);

                    // Update the name attributes
                    $clone
                        .find('[name="parcela"]').attr('name', 'financ[' + bookIndex + '].parcela').end()
                        .find('[name="valor"]').attr('name', 'financ[' + bookIndex + '].valor').end()
                        .find('[name="desconto"]').attr('name', 'financ[' + bookIndex + '].desconto').end()
                        .find('[name="dtVencimento"]').attr('name', 'financ[' + bookIndex + '].dtVencimento').end();

                    // Add new fields
                    // Note that we also pass the validator rules for new field as the third parameter
                    $('#empresaForm')
                        .formValidation('addField', 'financ[' + bookIndex + '].parcela',integerNotEmptyValidation)
                        .formValidation('addField', 'financ[' + bookIndex + '].valor',integerNotEmptyValidation)
                        .formValidation('addField', 'financ[' + bookIndex + '].dtVencimento',integerNotEmptyValidation);
                });
                $("select").select2({
                  placeholder: "Select a state",
                  allowClear: true
                });
            modal.close.then(function(result) {
                $scope.message = "You said " + result;
            });
        });
                }
            },
            {
                text: 'Entrada Via XML',
                key: '1',
                action: function (e, dt, node, config) {
                   ModalService.showModal({
            templateUrl: 'modalNfEntrada.html',
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
        DTColumnBuilder.newColumn(null).withTitle(titleHtml).notSortable()
            .renderWith(function(data, type, full, meta) {
                vm.selected[full.id] = false;
                return '<input type="checkbox" ng-model="showCase.selected[' + data.id + ']" ng-click="showCase.toggleOne(showCase.selected)"/>';
            }),

        DTColumnBuilder.newColumn('id').withTitle('ID'),     
        DTColumnBuilder.newColumn('fornecedor').withTitle('fornecedor'),
        DTColumnBuilder.newColumn('dataEntrada').withTitle('dataEntrada'),
         DTColumnBuilder.newColumn(null).withTitle('Produtos').notSortable()
            .renderWith(function(data, type, full, meta) {
                return '<a> '+ data.qntProd +'</a>';
            }),
       // DTColumnBuilder.newColumn('qntProd').withTitle('qntProd'), <span class="label label-info">Aguardando Conferencia</span>
        DTColumnBuilder.newColumn('valor').withTitle('valor'),
         DTColumnBuilder.newColumn(null).withTitle('Pedido Compra').notSortable()
            .renderWith(function(data, type, full, meta) {
                return '<a> '+ data.PedidoCompra +'</a>';
            }),
        DTColumnBuilder.newColumn('observacao').withTitle('observacao'),
        DTColumnBuilder.newColumn('modifyUser').withTitle('modifyUser').notVisible(),
        DTColumnBuilder.newColumn('modifyDateUTC').withTitle('modifyDateUTC').notVisible(),
         DTColumnBuilder.newColumn(null).withTitle('status').notSortable()
            .renderWith(function(data, type, full, meta) {
                return '<span class="label label-info">'+ data.status +'</span> ';
            }),
        DTColumnBuilder.newColumn(null).withTitle('Ações').notSortable().renderWith(actionsHtml)
    ];

    function edit(person) {
        ModalService.showModal({
            templateUrl: 'modalNfEntrada.html',
            controller: "RowSelectCtrl",
            resolve: {
                        params: function () {
                            debugger
                            console.log('value passed to modal:');
                            console.log(scope.value);
                            return scope.value;
                        }
                    }
        }).then(function(modal) {
            console.log('wwww')
            debugger
            modal.element.modal();
            modal.close.then(function(result) {
                $scope.message = "You said " + result;
            });
        });
                
    }
    function deleteRow(person) {
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
}
})();