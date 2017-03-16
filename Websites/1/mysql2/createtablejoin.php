<?php
$con = mysql_connect("localhost","root","password");
if (!$con)
{
die('Could not connect: ' . mysql_error());
}

// Create database
if (mysql_query("CREATE DATABASE jointest",$con))
{
echo "Database created";
}
else
{
echo "Error creating database: " . mysql_error();
}

// Create Persons table
mysql_select_db("jointest", $con);
$sql = "CREATE TABLE Persons
(
P_Id int NOT NULL AUTO_INCREMENT, 
PRIMARY KEY(P_Id),
FirstName varchar(15),
LastName varchar(15),
Address varchar(30), 
City varchar(15)
)";

// Execute query
mysql_query($sql,$con);


// Create Order table
mysql_select_db("jointest", $con);
$sql = "CREATE TABLE Orders
(
O_Id int NOT NULL AUTO_INCREMENT, 
PRIMARY KEY(O_Id),
OrderNo int,
P_Id int
)";

// Execute query
mysql_query($sql,$con);
mysql_query("INSERT INTO Persons (FirstName, LastName, Address, City) VALUES ('Ola', 'Hansen', 'Timoteivn10', 'Sandnes')");

mysql_query("INSERT INTO Persons (FirstName, LastName, Address, City) VALUES ('Tove', 'Svendson', 'Borgvn23', 'Sandnes')");

mysql_query("INSERT INTO Persons (FirstName, LastName, Address, City) VALUES ('Kari', 'Pettersen', 'Storgt20', 'stavanger')");


mysql_query("INSERT INTO Orders (OrderNo, P_Id)
VALUES (77895, 3)");
mysql_query("INSERT INTO Orders (OrderNo, P_Id)
VALUES (44678, 3)");
mysql_query("INSERT INTO Orders (OrderNo, P_Id)
VALUES (22456, 1)");
mysql_query("INSERT INTO Orders (OrderNo, P_Id)
VALUES (24562, 1)");
mysql_query("INSERT INTO Orders (OrderNo, P_Id)
VALUES (34764, 15)");

$result = mysql_query("SELECT * FROM Persons");

echo "<table border='1'>
<tr>
<th>P_Id</th>
<th>Firstname</th>
<th>Lastname</th>
<th>Address</th>
<th>City</th>
</tr>";

while($row = mysql_fetch_array($result))
{
echo "<tr>";
echo "<td>" . $row['P_Id'] . "</td>";
echo "<td>" . $row['FirstName'] . "</td>";
echo "<td>" . $row['LastName'] . "</td>";
echo "<td>" . $row['Address'] . "</td>";
echo "<td>" . $row['City'] . "</td>";
echo "</tr>";
}
echo "</table>";


$result = mysql_query("SELECT * FROM Orders");

echo "<table border='1'>
<tr>
<th>O_Id</th>
<th>OrderNo</th>
<th>P_Id</th>
</tr>";

while($row = mysql_fetch_array($result))
{
echo "<tr>";
echo "<td>" . $row['O_Id'] . "</td>";
echo "<td>" . $row['OrderNo'] . "</td>";
echo "<td>" . $row['P_Id'] . "</td>";
echo "</tr>";
}
echo "</table>";


mysql_close($con);
?>
