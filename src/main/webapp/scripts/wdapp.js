console.log("angular object",angular); //used for debugging and training only do not put in production
var WebDaptiveAppConfig = {
	//Main area for config URLs for WebDaptive

	/* When set to false a query parameter is used to pass on the auth token.
	 * This might be desirable if headers don't work correctly in some
	 * environments and is still secure when using https. */
	useAuthTokenHeader: true,
	//string to check for rest calls to jDpative Back-end so the angular http provider can inject x-auth token
	restAuthBase: 'qat-sysmgmt-controller-rest',
	authenticationURL: 'http://localhost:8080/springmvc-angularjs/api/authenticate',
	base_county_url: 'http://localhost:8080/qat-sysmgmt-controller-rest/county/api',
	base_procedure_url: 'http://localhost:8080/qat-sysmgmt-controller-rest/procedure/api',
	fetch_url: '/fetchPage',
	refresh_url: '/refresh',
	create_url: '/insert',
	update_url: '/update',
	delete_url: '/delete',

	//site
	base_site_url: 'http://localhost:8080/qat-sysmgmt-controller-rest/site/api',
	fetch_url: '/fetchPage',
	refresh_url: '/refresh',
	create_url: '/insert',
	update_url: '/update',
	delete_url: '/delete',

	//produto
	base_site_url: 'http://localhost:8080/qat-sysmgmt-controller-rest/produto/api',
	fetch_url: '/fetchPage',
	refresh_url: '/refresh',
	create_url: '/insert',
	update_url: '/update',
	delete_url: '/delete',

	base_empresa_url: 'http://localhost:8080/qat-sysmgmt-controller-rest/entidade/api',

};

(function() {
  'use strict';
		var wdApp = angular.module('wdApp',
				['ngRoute', 'ngAnimate', 'ui.bootstrap', 'easypiechart', 'textAngular',
				'ui.tree', 'ngMap', 'ngTagsInput', 'toastr', 'angular-loading-bar', 'chart.js', 'ngecharts',
				'agGrid', 'base64', 'LocalStorageModule', 'wdApp.controllers', 'wdApp.directives', 'wdApp.httpint',
				'wdApp.localization', 'wdApp.ui.controllers', 'wdApp.forms.controllers',
				'wdApp.forms.directives', 'wdApp.tables.controllers', 'wdApp.tasks',
				'wdApp.charts.flot.controllers', 'wdApp.charts.morris.controllers', 'wdApp.charts.chartjs.controllers',
				'wdApp.charts.other.controllers', 'wdApp.charts.echarts.controllers', 'wdApp.charts.directives', 'wdApp.authentication',
				'wdApp.pages.controllers', 'wdApp.demodata', 'wdApp.apps.stocks', 'wdApp.apps.stocksdata',
				'wdApp.apps.counties','datatables','datatables.bootstrap','wdApp.apps.estado', 'wdApp.apps.procedures','wdApp.apps.sysmgmt.data','wdApp.apps.site','wdApp.apps.empresa','wdApp.apps.produtos','wdApp.apps.produtoss','wdApp.apps.produto','wdApp.apps.notaFiscal',
				'wdApp.apps.pdCompras','wdApp.apps.orcamento','wdApp.apps.ordemServico','wdApp.apps.nfEntrada','wdApp.apps.pdCompras'
				,'wdApp.apps.cotacao','wdApp.apps.pdVendas','wdApp.apps.contasPagar','wdApp.apps.formaPg','wdApp.apps.contasReceber','wdApp.apps.banco','wdApp.apps.agencia','wdApp.apps.conta','wdApp.apps.funcionario',
				'wdApp.apps.cliente'
				,'wdApp.apps.fornecedor'
				,'wdApp.apps.convenio'
				,'wdApp.apps.deposito'
				,'wdApp.apps.filial'
				,'wdApp.apps.transportador'
				,'wdApp.apps.categoria'
				,'wdApp.apps.marca'
				,'wdApp.apps.unimed'
				,'wdApp.apps.ordemProducao'
				,'wdApp.apps.servico'
				,'wdApp.apps.funcionarios'
				,'wdApp.apps.compras'
				,'wdApp.apps.pagamentos'
				,'wdApp.apps.convenio'
				,'wdApp.apps.util'
				,'wdApp.apps.cidade'
				,'wdApp.apps.filial'
				,'wdApp.apps.almoxarifado'
				,'wdApp.apps.processo'
				]);

	wdApp.config(['$routeProvider',
		function($routeProvider) {
			var routes, setRoutes;
			//all available routes

			routes = ['dashboard','principal', 'ui/typography', 'ui/buttons','index3',
			'ui/icons', 'ui/grids', 'ui/widgets', 'ui/components',
			'ui/timeline', 'ui/tree', 'ui/pricing-tables', 'ui/maps',
			'tables/static', 'tables/dynamic', 'tables/responsive',
			'forms/elements', 'forms/layouts', 'forms/validation', 'forms/wizard',
			'charts/charts', 'charts/flot', 'charts/morris', 'charts/chartjs', 'charts/echarts',
			'pages/404', 'pages/500', 'pages/blank', 'pages/invoice', 'pages/profile',
			'pages/signin', 'mail/compose', 'mail/inbox', 'mail/single', 'tasks/tasks','empresa/cadEmpresa',
			'apps/stocks', 'apps/counties', 'apps/procedures', 'estado/apps/estado', 'produto/tables/produto', 'vendas/forms/cadNotaFiscal',
			'fiscal/forms/CadCfop','fiscal/tables/cfop','fiscal/forms/CadCnae','fiscal/tables/cnae',
			'vendas/forms/CadOrcamento','vendas/tables/orcamento','vendas/forms/CadOrdemServico','vendas/tables/ordemServico','vendas/tables/pedidoVendas',
			'compras/forms/CadCotacao','compras/forms/CadNfEntrada','compras/forms/CadPedCompras','compras/forms/CadAprovPedidoCompra',
			'compras/tables/aprovarPedCompras','compras/tables/cotacao','compras/tables/nfEntrada','compras/tables/pedCompras',
			'financeiro/tables/baixaTitulo','financeiro/tables/banco','financeiro/tables/caixa','vendas/tables/notaFiscalSaida',
			'financeiro/tables/contaCC','financeiro/tables/contasPagar','financeiro/tables/contasReceber','financeiro/tables/formaPg','financeiro/tables/agencia',
			'funcionario/tables/folhaPonto','funcionario/tables/funcionario','funcionario/forms/CadFolhaPonto','funcionario/forms/funcionario',
			'ordemServico/tables/ordemServico','ordemServico/forms/CadOrdemServico',
			'cadastros/tables/almoxarifado','cadastros/tables/cidade','cadastros/tables/cliente','cadastros/tables/convenio',
			'cadastros/tables/estado','cadastros/tables/filial','cadastros/tables/fornecedor','cadastros/tables/transportador',
			'produto/tables/categoria','produto/tables/marca','produto/tables/uniMed','produto/tables/servico',
			'produto/forms/CadCategoria','produto/forms/CadMarca','produto/forms/CadUniMed','produto/forms/CadProduto'
			,'advogado/tables/processo'
			,'advogado/details/processo'
			];

			//geeric routeine for building route from array
			setRoutes = function(route) {
				var config, url;
				url = '/' + route;
				config = {
				templateUrl: 'views/' + route + '.html'
				};
				$routeProvider.when(url, config);
				return $routeProvider;
			};

			//build routes for this application
			routes.forEach(function(route) {
				return setRoutes(route);
			});

			//routes not automatically built specified here
			return $routeProvider.when('/', {
				redirectTo: '/dashboard'
			}).when('/404', {
				templateUrl: 'views/pages/404.html'
			}).otherwise({
				redirectTo: '/404'
			});

	}]);

	//config
	wdApp.config(['localStorageServiceProvider', function(localStorageServiceProvider) {
		//sets local storage application prefix for all keys
		localStorageServiceProvider.setPrefix('wdAppLS');
	}]);

	//config
	wdApp.config(['$httpProvider', function($httpProvider) {
			//register WebDaptive interceptor factory
			$httpProvider.interceptors.push('WDHttpInterceptors');
	}]);

	//run each time angular app comes up (runs only once)
	wdApp.run(function($rootScope, $location, localStorageService) {

		$rootScope.main = {
			brand: 'E-Contábil',
			name: 'Jonh Snow'
		};

		/* Reset error when a new view is loaded */
		$rootScope.$on('$viewContentLoaded', function() {
			delete $rootScope.error;
		});

		//the hasRole method that drives the hide/show of html
		$rootScope.hasRole = function(role) {

			if ($rootScope.user === undefined) {
				return false;
			}

			if ($rootScope.user.roles[role] === undefined) {
				return false;
			}

			return $rootScope.user.roles[role];
		};

		//logout function clears all relevant presistent storage
		$rootScope.logout = function() {
			delete $rootScope.user;
			delete $rootScope.authToken;
			delete $rootScope.callingPath;
			delete $rootScope.displayRoles;
			$rootScope.main.name = "Jonh Snow";
			localStorageService.clearAll();
		};

		/* Try getting valid user from localStorage */
		if (localStorageService.get('authToken') !==  null){
			$rootScope.authToken = localStorageService.get('authToken');
			$rootScope.user = localStorageService.get('currentUser');
			$rootScope.main.name = 	$rootScope.user.user;
			$rootScope.displayRoles = localStorageService.get('displayRoles');
		}

		 //flag to let us know everything is full initialized can be referenced anywhere
		$rootScope.initialized = true;
		$rootScope.empresa_type = 0;
		localStorageService.set('empresaType', 0);
	});

})();
