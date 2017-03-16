<?php
$con = mysql_connect("localhost","root","password");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db("jointest", $con);

$result = mysql_query("
SELECT Persons.LastName, Persons.FirstName, Orders.OrderNo
FROM Persons
LEFT JOIN Orders
ON Persons.P_Id=Orders.P_Id
ORDER BY Persons.LastName
");

echo "<table border='1'>
<tr>
<th>Last Name</th>
<th>First Name</th>
<th>Order Number</th>
</tr>";

while($row = mysql_fetch_array($result))
  {
  echo "<tr>";
  echo "<td>" . $row['LastName'] . "</td>";
  echo "<td>" . $row['FirstName'] . "</td>";
  echo "<td>" . $row['OrderNo'] . "</td>";
  echo "</tr>";
  }
echo "</table>";




mysql_close($con);
?>
