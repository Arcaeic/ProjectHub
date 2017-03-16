<html>
<head>
 <title>Delete Form</title>
  <link rel="stylesheet" href="CSS/ControlPanel.css"/>
 </head>
<body>

<?php

$con = mysql_connect("localhost","root","password");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db("GameGrumps", $con);

$result = mysql_query("SELECT * FROM Characters ORDER BY Id DESC LIMIT 10");

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

<div>
<p> Delete by ID </p>

<form action="PHP/delete3.php" method="post">
ID: <input type="text" name="Id">
<input type="submit" />
</form>
 <br/>
  <br/>
 </div>

<div> 
<p> Deleting by Name of Series. </p>

<form action="PHP/delete2.php" method="post">
Name: <input type="text" name="Name" />
<input type="submit" />
</form>
 <br/>
  <br/>
 </div>

<div>
<p> Deleting by Winner </p>

<form action="PHP/delete1.php" method="post">
Winner: <input type="text" name="Winner" />
<input type="submit" />
</form>
 <br/>
  <br/>
 </div>

<div>
<p> Deleting by Normal or Vs </p>

<form action="PHP/delete.php" method="post">
Type: <input type="text" name="Type" />
<input type="submit" />
</form>
 <br/>
  <br/>
 </div>

<a id="back" href="http://localhost/mysql2/ControlPanel.php">Back</a>

</body>
</html>



