angular.module("wdApp.apps.produto", ["ngTable","datatables"]);

/*

(function() {
angular.module('wdApp.apps.produto', ['datatables'])
.controller('DataReloadWithAjaxCtrl', DataReloadWithAjaxCtrl);

function DataReloadWithAjaxCtrl(DTOptionsBuilder, DTColumnBuilder) {
    var vm = this;
    debugger
    vm.dtOptions = DTOptionsBuilder.fromSource('data.json')
        .withOption('stateSave', true)
        .withPaginationType('full_numbers');
    vm.dtColumns = [
        DTColumnBuilder.newColumn('id').withTitle('ID'),
        DTColumnBuilder.newColumn('firstName').withTitle('First name'),
        DTColumnBuilder.newColumn('lastName').withTitle('Last name').notVisible()
    ];
    vm.newSource = 'data1.json';
    vm.reloadData = reloadData;
    vm.dtInstance = {};

    function reloadData() {
        var resetPaging = false;
        vm.dtInstance.reloadData(callback, resetPaging);
    }

    function callback(json) {
        console.log(json);
    }
}
})();

(function() {
  angular.module('wdApp.apps.produto', ['datatables', 'datatables.tabletools'])
.controller('WithTableToolsCtrl', WithTableToolsCtrl);

function WithTableToolsCtrl(DTOptionsBuilder, DTColumnBuilder) {
    var vm = this;
    vm.dtOptions = DTOptionsBuilder
        .fromSource('data.json')
        // Add Table tools compatibility
        .withTableTools('vendor/datatables-tabletools/swf/copy_csv_xls_pdf.swf')
        .withTableToolsButtons([
            'copy',
            'print', {
                'sExtends': 'collection',
                'sButtonText': 'Save',
                'aButtons': ['csv', 'xls', 'pdf']
            }

        ]);
    vm.dtColumns = [
        DTColumnBuilder.newColumn('id').withTitle('ID').withClass('text-danger'),
        DTColumnBuilder.newColumn('firstName').withTitle('First name'),
        DTColumnBuilder.newColumn('lastName').withTitle('Last name')
    ];
}
})();
*/
(function() {
angular.module('wdApp.apps.produto', ['datatables', 'datatables.buttons', 'datatables.light-columnfilter'])
    .controller('WithButtonsCtrl', WithButtonsCtrl);

function WithButtonsCtrl(DTOptionsBuilder, DTColumnBuilder) {
    var vm = this;
    vm.dtOptions = DTOptionsBuilder.fromSource('data.json')
        .withDOM('frtip')
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
            'columnsToggle',
            'colvis',
            'copy',
            'print',
            'excel',
            {
                text: 'Some button',
                key: '1',
                action: function (e, dt, node, config) {
                    alert('Button activated');
                }
            }
        ]);
    vm.dtColumns = [
        DTColumnBuilder.newColumn('id').withTitle('ID'),
        DTColumnBuilder.newColumn('firstName').withTitle('First name'),
        DTColumnBuilder.newColumn('lastName').withTitle('Last name')
    ];
}
})();

(function() {
  "use strict";

  angular.module("wdApp.apps.produto").controller("produtoController",
  ['$scope', 'SysMgmtData', 'toastr','NgTableParams', '$element',
	function($scope, SysMgmtData, toastr,NgTableParams,$element) {
		var self = this;

		self.simpleList = [{
          "name": "aab",
          "age": 5,
          "money": 5
        },
        {
          "name": "aac",
          "age": 55,
          "money": 0
        },
        {
          "name": "aad",
          "age": 555,
          "money": 1
        },
        {
          "name": "aae",
          "age": 5555,
          "money": 2
        },
        {
          "name": "aaf",
          "age": 55555,
          "money": 3
        },
        {
          "name": "aag",
          "age": 555555,
          "money": 4
        }]

		$scope.tabs = [{
            title: 'Produto',
            url: 'one.tpl.html'
        }, {
            title: 'Dados Tributarios',
            url: 'two.tpl.html'
        }, {
            title: 'Preço/Estoque',
            url: 'three.tpl.html'
        }, {
            title: 'Imagens',
            url: 'imagens'
        }, {
            title: 'Historico',
            url: 'historico'

    }];

    self.cols = [
      { field : "name", valueExpr: "name", title: "Name",  filter: { name: "text" }, sortable: "name", show: true },
      { field: "age", title: "Age",sortable: "age",show: true },
      { field: "money", title: "Money", show: true }
    ];
    self.tableParams = new NgTableParams({}, {
      dataset: angular.copy(self.simpleList)
    });




    $scope.currentTab = 'one.tpl.html';

    $scope.onClickTab = function (tab) {
        $scope.currentTab = tab.url;
    }
    $scope.onClickTabb = function (tab) {
       debugger
    }

    $scope.isActiveTab = function(tabUrl) {
        return tabUrl == $scope.currentTab;
    }

	$scope.submitt = function() {

		console.log($scope.produto)
		debugger
		//fnMontaObjeto();
		//console.log($scope.empresa)
		//processPostData(create_url, new qat.model.reqEmpr($scope.empresa ,true, true), true);
	};
    $scope.produto = [];
	$scope.produto.aliquotaCOFINSST="10";
	$scope.produto.aliquotaIPI="10";
	$scope.produto.anotainternas="Anotações internas";
	$scope.produto.cEST="1";
	$scope.produto.cFOPPadraoNFe="50201";
	$scope.produto.cNPJProdutor="057102014";
	$scope.produto.cOFINSSituatributaria="49";
	$scope.produto.categoria="08";
	$scope.produto.cdBarras="1111101041010";
	$scope.produto.classeCigarrosBebidas="Classe cigarros e bebidas";
	$scope.produto.codControleIPI="10";
	$scope.produto.codEnquadramento="999";
	$scope.produto.codigo="00001";
	$scope.produto.dataCadastro="11/06/2016";
	$scope.produto.estAtual="10";
	$scope.produto.estMaximo="50";
	$scope.produto.estMinimo="10";
	$scope.produto.excecaoIPI="1";
	$scope.produto.iCMSOrigem="0";
	$scope.produto.iPISitTributaria="00";
	$scope.produto.icmsSitTributaria="90";
	$scope.produto.informAdicionaisParaNFe="Inform. adicionais para a NFe";
	$scope.produto.margemLucro="1;00";
	$scope.produto.nCM="123456789";
	$scope.produto.nome="Produto";
	$scope.produto.pISSituaTributaria="01";
	$scope.produto.pesobruto="1,0";
	$scope.produto.pesoliquido="1,00";
	$scope.produto.precoCusto="1.00";
	$scope.produto.precoVenda="1.01";
	$scope.produto.qtdSeloIPI="1";
	$scope.produto.tipoCalculo="2";
	$scope.produto.tipoCalculoSubstTrib="1";
	$scope.produto.tipocalculoSubstTrib="1";
	$scope.produto.unidTributada="02";
	$scope.produto.valorTribCOFINS="10";
	$scope.produto.valorTribPISST="05";
	$scope.produto.valorUnidtribPIS="10";


	self.checkboxes = {
      checked: false,
      items: {}
    };

    // watch for check all checkbox
    $scope.$watch(function() {
      return self.checkboxes.checked;
    }, function(value) {
      angular.forEach(self.simpleList, function(item) {
        self.checkboxes.items[item.id] = value;
      });
    });

    // watch for data checkboxes
    $scope.$watch(function() {
      return self.checkboxes.items;
    }, function(values) {
      var checked = 0, unchecked = 0,
          total = self.simpleList.length;
      angular.forEach(self.simpleList, function(item) {
        checked   +=  (self.checkboxes.items[item.id]) || 0;
        unchecked += (!self.checkboxes.items[item.id]) || 0;
      });
      if ((unchecked == 0) || (checked == 0)) {
        self.checkboxes.checked = (checked == total);
      }
      // grayed checkbox
      angular.element($element[0].getElementsByClassName("select-all")).prop("indeterminate", (checked != 0 && unchecked != 0));
    }, true);


	}
  ])

})();

(function() {
  "use strict";

  angular.module("wdApp.apps.produto").run(configureDefaults);
  configureDefaults.$inject = ["ngTableDefaults"];

  function configureDefaults(ngTableDefaults) {
    ngTableDefaults.params.count = 5;
    ngTableDefaults.settings.counts = [];
  }
})();


/*


var testTableApp = angular.module( 'testTableApp', ['ngRoute', 'ngResource', 'datatables', 'datatables.tabletools', 'datatables.bootstrap', 'datatables.fixedheader'] );
console.log( testTableApp );
testTableApp.controller("mainTable", 
[ '$scope', 'DTOptionsBuilder', 'DTColumnBuilder',
    function ( $scope, DTOptionsBuilder, DTColumnBuilder){
        $scope.dataSource = "http://dt.ishraf.com/ajax.php";
        $scope.start = 0;
        $scope.end = 5000;
        
        
        $scope.getDataSource = function(obj,prefix){
            var src = $scope.dataSource;
            
            var str = [];
            for(var p in obj) {
                if (obj.hasOwnProperty(p)) {
                    var k = prefix ? prefix + "[" + p + "]" : p, v = obj[p];
                    str.push(typeof v == "object" ?
                    serialize(v, k) :
                    encodeURIComponent(k) + "=" + encodeURIComponent(v));
                }
            }
            return src + "?" + str.join("&");
        }
        
        var dsParams = {
            start : $scope.start,
            end : $scope.end
        }
        
        $scope.dsString = $scope.getDataSource( dsParams );
        
        
        $scope.buildTable = function(){
            return DTOptionsBuilder
                .newOptions()
                .withOption('ajax', {
                    // Either you specify the AjaxDataProp here
                    dataSrc: 'data',
                    url: $scope.dsString,
                    type: 'POST'
                }).
                withOption( 'lengthMenu', [
                    [10, 20, 50, 100, 150, 300, 500],
                    [10, 20, 50, 100, 150, 300, 500]
                ])                
                .withTableTools('bower_components/datatables-tabletools/swf/copy_csv_xls_pdf.swf')
                .withTableToolsButtons([
                    {
                        "sExtends": "copy",
                        "sButtonText": "<i class='fa fa-copy'></i>&nbsp;|&nbsp;Copy",
                        "fnInit": function (nButton, oConfig) {
                            $(nButton).addClass('btn btn-success');
                        }
                    },
                    {
                        "sExtends": "print",
                        "sButtonText": "<i class='fa fa-print'></i>&nbsp;|&nbsp;Print",
                        "fnInit": function (nButton, oConfig) {
                            $(nButton).addClass('btn btn-danger');
                        }
                    },
                    {
                        "sExtends": "csv",
                        "sButtonText": "<i class='fa fa-file-o'></i>&nbsp;|&nbsp;CSV",
                        "fnInit": function (nButton, oConfig) {
                            $(nButton).addClass('btn btn-primary');
                        }
                    },
                    {
                        "sExtends": "pdf",
                        "sButtonText": "<i class='fa fa-file-pdf-o'></i>&nbsp;|&nbsp;PDF",
                        "fnInit": function (nButton, oConfig) {
                            $(nButton).addClass('btn btn-warning');
                        }
                    }
                ])
                .withFixedHeader({
                    bottom: true
                })
                .withDOM('<"clear"><"#top.hidden-print"<".row"<".col-md-6"i><".col-md-6"f>><".row"<".col-md-6"l><".col-md-6"p>><"clear">T>rt')
                ;            
        }
        
        
        $scope.dtOptions = $scope.buildTable();
            
        $scope.buildColumns = function(){
            return [
                DTColumnBuilder.newColumn('id').withTitle('ID'),
                DTColumnBuilder.newColumn('firstName').withTitle('First name'),
                DTColumnBuilder.newColumn('lastName').withTitle('Last name'),
                DTColumnBuilder.newColumn('city').withTitle('city'),
                DTColumnBuilder.newColumn('state').withTitle('state'),
                DTColumnBuilder.newColumn('zip').withTitle('zip'),
                DTColumnBuilder.newColumn('country').withTitle('country'),
                DTColumnBuilder.newColumn('phone').withTitle('phone'),
                DTColumnBuilder.newColumn('email').withTitle('email')
            ];
        }
        
        $scope.dtColumns = $scope.buildColumns();
        
        
        $scope.reloadData = reloadData;
        $scope.dtInstance = {};

        function reloadData() {
            var resetPaging = false;
            $scope.dtInstance.reloadData(callback, resetPaging);
        }

        function callback(json) {
            console.log(json);
        }
        
    }
]);


*/