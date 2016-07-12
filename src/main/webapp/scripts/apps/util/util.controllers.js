
(function() {
angular.module('wdApp.apps.util', ['angularModalService'])
    .controller('UtilController', utilControllers);

function utilControllers($scope,ModalService) {
    
    $scope.empresaType  = 0 
    var vm = this;



    ModalService.showModal({
        templateUrl: 'dashboardDialog.html',
        controller: "NavController"
    }).then(function(modal) {

        modal.element.modal();
        modal.close.then(function(result) {
            $scope.message = "You said " + result;
        });
    });

}
})();