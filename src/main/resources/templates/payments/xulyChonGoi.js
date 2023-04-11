const app = angular.module('paymentApp', []);
app.controller('paymentController', function($scope, $http) {
    $scope.items = [];

    $scope.initialize = function() {
        $http.get('/subscription_pack/all').then(function(response) {
            $scope.items = response.data;
            console.log($scope.items);
            
        }
        , function(error) {
            console.log(error);
        }
        );
    }

    $scope.initialize();
});