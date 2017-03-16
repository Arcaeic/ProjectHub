<html>
<head>
<link rel="stylesheet" href="http://localhost/mysql2/CSS/ControlPanel.css"/>
<title>All Entries</title>
</head>
<body>
<?php
$con = mysql_connect("localhost","root","password");
mysql_select_db("GameGrumps", $con);
$result = mysql_query("SELECT * FROM Characters");

echo "<table border='1'>
<tr>
<th>ID</th>
<th>Date</th>
<th>Normal or Vs</th>
<th>Name of Series</th>
<th>Genre</th>
<th>Winner</th>
</tr>";

while($row = mysql_fetch_array($result ))
  {
  echo "<tr>";
  echo "<td>" . $row['Id'] . "</td>";
  echo "<td>" . $row['Date'] . "</td>";
  echo "<td>" . $row['Type'] . "</td>";
  echo "<td>" . $row['Name'] . "</td>";
  echo "<td>" . $row['Genre'] . "</td>";
  echo "<td>" . $row['Winner'] . "</td>";
  echo "</tr>";
  }
echo "</table>";
echo "<br/>";
echo "<br/>";
          ?>
<br/>
<br/>
<a href="http://192.168.47.128/mysql2/ControlPanel.php">Back</a>
</body>
</html>
