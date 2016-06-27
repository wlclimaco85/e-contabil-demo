(function() {
  angular.module('wdApp.apps.notaFiscal', []).controller('NotaFiscalController',
  ['$scope', 'SysMgmtData', 'toastr', 'toastrConfig',
	function($scope, SysMgmtData, toastr, toastrConfig) {
		var cvm = this;

		//principal
		$scope.tabs = [{
            title: 'Dados da NF-e',
            url: 'dadosNFe'
        }, {
            title: 'Emitente',
            url: 'emitente'
        }, {
            title: 'Destinatário/Remetente',
            url: 'destinatario'
        }, {
            title: 'Produto e Serviços',
            url: 'produtos'
        }, {
            title: 'Totais',
            url: 'totais'
    	}, {
            title: 'Transporte',
            url: 'transposte'
    	}, {
            title: 'Cobrança',
            url: 'cobranca'
    	}, {
            title: 'Informaçoes Adicionais',
            url: 'infoAdd'
    	}, {
            title: 'Exportação e Compras',
            url: 'exportacao'
    	}, {
            title: 'Cana',
            url: 'cana'
    	}, {
            title: 'Autorização Download',
            url: 'download'
    	}
    ];

    $scope.currentTab = 'dadosNFe';

    $scope.onClickTab = function (tab) {
        $scope.currentTab = tab.url;
    }

    $scope.isActiveTab = function(tabUrl) {
        return tabUrl == $scope.currentTab;
    }

    $scope.tabsDados = [{
            title: 'NF-e',
            url: 'NFe'
        }, {
            title: 'Notas e Conhecimentos Fiscais Referenciados',
            url: 'conhecimentos'
        }, {
            title: 'Notas Fiscais Referenciadas de Produtor',
            url: 'produtor'
        }, {
            title: 'Cupons Fiscais Vinculados a NF-e',
            url: 'cupons'
        }
    ];


    $scope.currentTabDados = 'NFe';


    $scope.onClickTabDados = function (tab) {
       $scope.currentTabDados = tab.url;
    }


    $scope.isActiveTabDados = function(tabUrl) {
        return tabUrl == $scope.currentTabDados;
    }

    //totais
    $scope.tabsTotais = [{
            title: 'ICMS',
            url: 'icms'
        }, {
            title: 'ISSQN',
            url: 'issqn'
        }, {
            title: 'Retenção de Tributos',
            url: 'tributos'
        }
    ];


    $scope.currentTabTotais = 'icms';


    $scope.onClickTabTotais = function (tab) {
       $scope.currentTabTotais = tab.url;
    }


    $scope.isActiveTabTotais = function(tabUrl) {
        return tabUrl == $scope.currentTabTotais;
    }



    }
  ]);
}).call(this);

