angular.module("wdApp.apps.produto", ["ngTable"]);

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