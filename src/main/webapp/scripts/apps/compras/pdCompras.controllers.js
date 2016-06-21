(function() {
'use strict';
angular.module('wdApp.apps.pdCompras', ['datatables'])
.controller('PdComprasController', RowSelect);

function RowSelect($compile, $scope, DTOptionsBuilder, DTColumnBuilder) {
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
    vm.selected = {};
    vm.selectAll = false;

    var titleHtml = '<input type="checkbox" ng-model="showCase.selectAll"' +
        'ng-click="showCase.toggleAll(showCase.selectAll, showCase.selected)">';




    vm.dtOptions = DTOptionsBuilder.fromSource('notaEntrada.json')
        .withOption('createdRow', function(row, data, dataIndex) {
            // Recompiling so we can bind Angular directive to the DT
            $compile(angular.element(row).contents())($scope);
        })
        .withOption('headerCallback', function(header) {
            if (!vm.headerCompiled) {
                // Use this headerCompiled field to only compile header once
                vm.headerCompiled = true;
                $compile(angular.element(header).contents())($scope);
            }
        })
        .withDOM('<"row tablealign"<"col-xs-6 col-md-4" C> <"col-xs-6  col-md-4" f>>t<\'row\'<\'col-xs-5\'i><\'col-xs-3\'l><\'col-xs-4\'p>>')
        //.withDOM('frtip')
        .withPaginationType('full_numbers')
        .withOption('createdRow', createdRow)
            //.withDataProp('data')
            .withOption('serverSide', true)
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
        extend: "pdfHtml5",
        fileName:  "Data_Analysis",
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
        autoPrint: false,
        exportOptions: {
            columns: ':visible'
        }
    },
    {
        extend: "csvHtml5"
        
    }
]);
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


  $scope.oneAtATime = true;

  $scope.groups = [
    {
      title: 'Dynamic Group Header - 1',
      content: 'Dynamic Group Body - 1'
    },
    {
      title: 'Dynamic Group Header - 2',
      content: 'Dynamic Group Body - 2'
    }
  ];

  $scope.items = ['Item 1', 'Item 2', 'Item 3'];

  $scope.addItem = function() {
    var newItemNo = $scope.items.length + 1;
    $scope.items.push('Item ' + newItemNo);
  };

  $scope.status = {
    isCustomHeaderOpen: false,
    isFirstOpen: true,
    isFirstDisabled: false
  };

}

})();