<html>
<body>

<?php
echo "Hello World";
?>

<?php
//This is a comment

/*
This is
a comment
block
*/
?>

<?php
$myCar="null";
?>

<?php
$txt="Hello World";
$x=16;
?>

<?php
$1=x;
switch ($x)
{
case 1:
    echo "Number 1";
    break;
case 2:
    echo "Number 2";
    break;
case 3:
    echo "Number 3";
    break;
default:
    echo "No number between 1 and 3";
}
?>

<?php
$cars1="Saab";
$cars2="Volvo";
$cars3="BMW";
?>

<?php
$cars[0]="Saab";
$cars[1]="Volvo";
$cars[2]="BMW";
$cars[3]="Toyota";
echo $cars[0] . " and " . " are Swedish cars.";
?>

<?php
while (condition)
    {
   code to be executed;
    }
?>


<?php
$x=array ("one","two","three","four","five");
$value="three";
foreach ($x as $value)
    {
     echo $value . "<br />";
}
?>

<?php
function nameofFunction()
{
echo "This function is working!"
}
?>

<form action="welcome.php" method="get-or-post":>
Name: <input type="t5ext" name="fname" />
Age: <input type="text" name="age" />
<input type="submit" />
</form>
