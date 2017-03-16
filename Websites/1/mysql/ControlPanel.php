<html>
 <head>
  <link rel="stylesheet" href="CSS/ControlPanel.css"/>
   <title>MySQL Control Panel</title>
<div>
     <a href="selectform.html">Select</a>
    <a href="PHP/update.php">Update</a>
   <a href="insertform.html">Insert</a>
  <a href="PHP/delete.php">Delete</a>
 <a href="3.swf">Delete Database</a>
<div>
 <div align="right" position="relative">
 <?php
mysql_connect('localhost', 'root', 'password');
if (!mysql_select_db('my_db')) {
  echo '<a href="create.php">Create Database</a>';
}
else {
  echo '<a href="dropdatabase.php">Delete Database</a>';
}
 ?>
</div>
</div>
</div>
<br/>
<br/>
<object width="250" height="45" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" id="gsSong263930765" name="gsSong263930765"><param name="movie" value="http://grooveshark.com/songWidget.swf" /><param name="wmode" value="window" /><param name="allowScriptAccess" value="always" /><param name="flashvars" value="hostname=grooveshark.com&songID=26393076&style=metal&p=0" /><object type="application/x-shockwave-flash" data="http://grooveshark.com/songWidget.swf" width="250" height="40"><param name="wmode" value="window" /><param name="allowScriptAccess" value="always" /><param name="flashvars" value="hostname=grooveshark.com&songID=26393076&style=metal&p=0" /><span><a href="http://grooveshark.com/search/song?q=Avenged%20Sevenfold%20Welcome%20To%20The%20Family" title="Welcome To The Family by Avenged Sevenfold on Grooveshark">Welcome To The Family by Avenged Sevenfold on Grooveshark</a></span></object></object>
    </head>
     <body>
 <div>
  <div>
   <?php
$result = mysql_query("SELECT * FROM Persons");

echo "<table border='1'>
<tr>
<th>Firstname</th>
<th>Lastname</th>
<th>Age</th>
</tr>";

while($row = mysql_fetch_array($result))
  {
  echo "<tr>";
  echo "<td>" . $row['FirstName'] . "</td>";
  echo "<td>" . $row['LastName'] . "</td>";
  echo "<td>" . $row['Age'] . "</td>";
  echo "</tr>";
  }
echo "</table>";
echo "<br/>";
echo "<br/>";
          ?>
     </div>
    </div>
   <br/>
  <br/>
 </body>
</html>
