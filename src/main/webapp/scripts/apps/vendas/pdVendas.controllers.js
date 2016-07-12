(function() {
angular.module('wdApp.apps.pdVendas', ['datatables','angularModalService', 'datatables.buttons', 'datatables.light-columnfilter'])
.controller('PdVendasController', pdVendasController);

function pdVendasController($scope, $compile, DTOptionsBuilder, DTColumnBuilder,ModalService) {
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

    var titleHtml = '<input type="checkbox" ng-model="showCase.selectAll"' +
        'ng-click="showCase.toggleAll(showCase.selectAll, showCase.selected)">';

    vm.dtOptions = DTOptionsBuilder.fromSource('pedidoVendas.json')
        .withDOM('frtip')
        .withPaginationType('full_numbers')
        .withOption('createdRow', createdRow)
        .withOption('headerCallback', function(header) {
            if (!vm.headerCompiled) {
                // Use this headerCompiled field to only compile header once
                vm.headerCompiled = true;
                $compile(angular.element(header).contents())($scope);
            }
        })
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
                text: 'Novo Pedido Venda',
                key: '1',
                action: function (e, dt, node, config) {
                    ModalService.showModal({
                        templateUrl: 'modalPdVendas.html',
                        controller: "PdVendasController"
                    }).then(function(modal) {

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

                        modal.element.modal();
                        modal.close.then(function(result) {
                            $scope.message = "You said " + result;
                        });
                    });
                }
            }
        ]);
    vm.dtColumns = [
        DTColumnBuilder.newColumn(null).withTitle(titleHtml).notSortable()
            .renderWith(function(data, type, full, meta) {
                vm.selected[full.id] = false;
                return '<input type="checkbox" ng-model="showCase.selected[' + data.id + ']" ng-click="showCase.toggleOne(showCase.selected)"/>';
        }).withOption('width', '10px'),
        DTColumnBuilder.newColumn('id').withTitle('ID').withOption('width', '10px').notVisible(),   
        DTColumnBuilder.newColumn('cliente').withTitle('cliente').withOption('width', '150px'),
        DTColumnBuilder.newColumn('data').withTitle('data').withOption('width', '50px'),
         DTColumnBuilder.newColumn(null).withTitle('qntProd').notSortable().withOption('width', '10px')
            .renderWith(function(data, type, full, meta) {
                return '<a> '+ data.qntProd +'</a>';
            }),
       // DTColumnBuilder.newColumn('qntProd').withTitle('qntProd'),
        DTColumnBuilder.newColumn('valorPedido').withTitle('valorPedido').withOption('width', '15px'),
        DTColumnBuilder.newColumn('valorItens').withTitle('valorItens').withOption('width', '15px'),
        DTColumnBuilder.newColumn('observacao').withTitle('observacao').withOption('width', '150px'),
        DTColumnBuilder.newColumn('modifyUser').withTitle('modifyUser').notVisible(),
        DTColumnBuilder.newColumn('modifyDateUTC').withTitle('modifyDateUTC').notVisible(),
        DTColumnBuilder.newColumn('status').withTitle('status'),
        DTColumnBuilder.newColumn(null).withTitle('Ações').notSortable().renderWith(actionsHtml).withOption('width', '150px')
    ];

   

    function edit(person) {
       ModalService.showModal({
            templateUrl: 'modalPdVendas.html',
            controller: "PdVendasController"
        }).then(function(modal) {
            modal.element.modal();
            modal.close.then(function(result) {
                $scope.message = "You said " + result;
            });
        });
    }
    function deleteRow(person) {
        ModalService.showModal({
            templateUrl: 'cnaeDelete.html',
            controller: "PdVendasController"
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
            '<li><a href="javaScript:;" ng-click="showCase.edit(showCase.persons[' + data.id + '])">Alterar</a></li>'+
            '<li role="separator" class="divider"></li>'+
            '<li><a href="#">Transformar Em NF-e</a></li>'+
          '</ul>'+
        '</div>'
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