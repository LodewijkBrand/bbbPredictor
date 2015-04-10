<?php
$smiles = $_GET['smiles_string'];
$command = escapeshellcmd('python predict.py '.$smiles); #escapeshellcmd sanitizes input from the user
$output = shell_exec($command);
echo $output;
?>
