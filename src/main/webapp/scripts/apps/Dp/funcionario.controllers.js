(function() {
angular.module('wdApp.apps.funcionario', ['datatables','angularModalService', 'datatables.buttons', 'datatables.light-columnfilter'])
.controller('FuncionarioController', funcionarioController);

function funcionarioController($scope, $compile, DTOptionsBuilder, DTColumnBuilder,ModalService) {
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

    vm.dtOptions = DTOptionsBuilder.fromSource('funcionario.json')
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
        .withOption('initComplete', function (settings,json) {
            
            $('.dt-buttons').find('.dt-button:eq(1)').before(

            '<select class="form-control col-sm-3 btn btn-primary dropdown-toggle" data-ng-options="t.name for t in vm.types"'+
              'data-ng-model="vm.object.type" style="height: 32px;margin-left: 8px;margin-right: 6px;width: 200px !important;">'+
              
                '<option><a href="#">Ações <span class="badge selected badge-danger main-badge" data-ng-show="{{showCase.countSeleted()}}"</span></a></option>'+
                '<option><a href="#">Remover Todos <span class="badge selected badge-danger main-badge"  data-ng-show="{{showCase.countSeleted()}}"></span></a></option>'+
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
                text: 'Novo Funcionario',
                key: '1',
                action: function (e, dt, node, config) {
                    ModalService.showModal({
                        templateUrl: 'formaPag.html',
                        controller: "ContasPagarController"
                    }).then(function(modal) {

                        
                        modal.element.modal();
                        openDialogUpdateCreate();
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
        DTColumnBuilder.newColumn('id').withTitle('ID').notVisible().withOption('width', '10px'), 
        DTColumnBuilder.newColumn('nome').withTitle('Nome'),
        DTColumnBuilder.newColumn('rg').withTitle('RG'),    
        DTColumnBuilder.newColumn('cpf').withTitle('CPF'),
        DTColumnBuilder.newColumn('nunCartTrab').withTitle('Nº Cart. Trab.'),
        DTColumnBuilder.newColumn('dtNasc').withTitle('Data Nasc.'),
        DTColumnBuilder.newColumn('cargHor').withTitle('Carga Hor. Semanal').notVisible(),
         DTColumnBuilder.newColumn(null).withTitle('Horarios')
            .renderWith(function(data, type, full, meta) {
                var sReturn = "";
                if(data.horarios != undefined)
                {
                    if(data.horarios.length > 0){
                        for(var x = 0;x<data.horarios.length;x++)
                        {
                            if((data.horarios[x].Entrada !== "")&&(data.horarios[x].Entrada != undefined)){
                                sReturn = sReturn + "<a> Entrada "+data.horarios[x].Entrada +"  </a><br>";
                            }else {
                                sReturn = sReturn + "<a> Saida "+data.horarios[x].Saida +"  </a><br>";
                            }
                        }
                    }     
                }
                
                return sReturn;
                
            }).withOption('width', '130px'),
        DTColumnBuilder.newColumn('cargo').withTitle('Cargo').notVisible(),
        DTColumnBuilder.newColumn('salario').withTitle('Salario').notVisible(),
        DTColumnBuilder.newColumn('dtAdmissao').withTitle('Data Admissão').notVisible(),
        DTColumnBuilder.newColumn('dtDemissao').withTitle('Data Demissão').notVisible(),
        DTColumnBuilder.newColumn('sexo').withTitle('Sexo').notVisible(),
        DTColumnBuilder.newColumn('estCivil').withTitle('Estado Civil').notVisible(),
        DTColumnBuilder.newColumn('qntFilhos').withTitle('Nº de Filhos').notVisible(),
        DTColumnBuilder.newColumn('cep').withTitle('Cep').notVisible(),    
        DTColumnBuilder.newColumn('logradouro').withTitle('Logradouro').notVisible(),
        DTColumnBuilder.newColumn('numero').withTitle('Numero').notVisible(),
        DTColumnBuilder.newColumn('cidade').withTitle('Cidade').notVisible(),
        DTColumnBuilder.newColumn('estado').withTitle('Estado').notVisible(),
        DTColumnBuilder.newColumn('telefone').withTitle('Telefone').notVisible(),
        DTColumnBuilder.newColumn('email').withTitle('Email').notVisible(),
        DTColumnBuilder.newColumn('obs').withTitle('Observações').notVisible(),
        DTColumnBuilder.newColumn('modifyUser').withTitle('modifyUser').notVisible(),
        DTColumnBuilder.newColumn('modifyDateUTC').withTitle('modifyDateUTC').notVisible(),
        DTColumnBuilder.newColumn(null).withTitle('Ações').notSortable().renderWith(actionsHtml).withOption('width', '140px'), 
    ];

   

    function edit(person) {
       ModalService.showModal({
            templateUrl: 'formaPag.html',
            controller: "ContasPagarController"
        }).then(function(modal) {
            
            modal.element.modal();
            openDialogUpdateCreate();
            modal.close.then(function(result) {
                $scope.message = "You said " + result;
            });
        });
    }
    function deleteRow(person) {
        ModalService.showModal({
            templateUrl: 'formaPgDelete.html',
            controller: "ContasPagarController"
        }).then(function(modal) {
            modal.element.modal();
            modal.close.then(function(result) {
                $scope.message = "You said " + result;
            });
        });
    }
    function openDialogUpdateCreate()
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


angular.module('wdApp.apps.funcionarios', ['datatables', 'datatables.bootstrap'])
.controller('WithResponsiveCtrl', WithResponsiveCtrl);

function WithResponsiveCtrl(DTOptionsBuilder, DTColumnBuilder) {
    var vm = this;
    vm.dtOptions = DTOptionsBuilder.fromSource('data.json')
        .withPaginationType('full_numbers')
        // Active Responsive plugin
        .withOption('responsive', true);
    vm.dtColumns = [
        DTColumnBuilder.newColumn('id').withTitle('ID'),
        DTColumnBuilder.newColumn('firstName').withTitle('First name'),
        // .notVisible() does not work in this case. Use .withClass('none') instead
        DTColumnBuilder.newColumn('lastName').withTitle('Saldo Total').withClass('none'),
        DTColumnBuilder.newColumn('lastName').withTitle('Saldo Total 2 ').withClass('none')
    ];
}