<html>

<?php

$predictor = new Predictor();

class Predictor{
    function __construct(){
        $this->showPredictor();
    }
    
    private function showPredictor(){
        echo '<label for="smiles_string">Input SMILES String:</label>';
        echo '<input type="text" id="smiles_string" />';
        echo '<button type="button" onclick=" predict(); ">Predict BBB Permeability</button>';
    }
}
?>

<body>

<script>
    function predict(){
        var smiles = document.getElementById("smiles_string").value;
        
        var req = new XMLHttpRequest();
        req.onload = receive_message;
        var url = "predictPermeability.php?";
        url = url + "smiles_string=" + document.getElementById("smiles_string").value;
        req.open("GET", url, true);
        req.send();
    }
    
    function receive_message(){
        var response = document.getElementById("predictorResponse");
        response.innerHTML = this.responseText;
    }
</script>

<div id = "predictorResponse">

</div>

</body>

</html>
