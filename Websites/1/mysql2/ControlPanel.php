<html>
 <head>
  <link rel="stylesheet" href="CSS/ControlPanel.css"/>
   <title>MySQL Control Panel</title>
<div>
      <a href="selectform.html">Select</a>
     <a href="PHP/where.php">Where</a>
    <a href="updateform.html">Update</a>
   <a href="insertform.php">Insert</a>
    <a href="PHP/join.php">Join</a>
   <a href="PHP/innerjoin.php">Inner Join</a>
<a href="PHP/fulljoin.php">Full Join</a>
<a href="PHP/leftjoin.php">Left Join</a>
<a href="PHP/rightjoin.php">Right Join</a>
   <a href="deleteform.php">Delete</a>
  <a href="http://192.168.47.128/Gifs/3.swf">Delete Database</a>
<div>
 <div align="right" position="relative">
 <?php
mysql_connect('localhost', 'root', 'password');
if (!mysql_select_db('GameGrumps')) {
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
$result = mysql_query("SELECT * FROM Characters ORDER BY Id");

echo "<table border='1'>
<tr>
<th colspan='6'>Game Grumps<th>
</tr>
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

     </div>
    </div>
<br/>
<br/>
<div>
<br/>
<br/>
<a href="PHP/showall.php">Show All Entries</a>
</div>
   <br/>
  <br/>
<p>Rules: <br/><br/> Normal - The winner is chosen by who was the most humorous of the two.<br/>
          VS - The winner is chosen by whomever beat the other person, regardless of being humorous.</p>
 </body>
</html>
