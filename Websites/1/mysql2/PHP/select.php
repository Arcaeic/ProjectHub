<html>
<head>
<link rel="stylesheet" href="CSS/ControlPanel.css"/>
<title>Select Form</title>
</head>
</html>

<?php
$con = mysql_connect("localhost","root","password");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db("GameGrumps", $con);

$result = mysql_query("SELECT * FROM Characters
WHERE Type='$_POST[Type]'");

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

<html>
<head>
<link rel="stylesheet" href="http://localhost/mysql2/CSS/ControlPanel.css"/>
</head>
<body>
<a href="http://localhost/mysql2/ControlPanel.php">Back</a>
</html>
