# Insert & Delete
Created Thursday 12 October 2017

Insert into Movies(year, title, type, shortname)
VALUES (1977, 'Escape From New York', 'Cyberpunk', 'EFNY'),
  (1980, 'Chef', 'Food', 'Chef'),
  (1958, 'Twilight Zone', 'Horror', 'TZ')
);


### Delete

Delete From Movies;	— Deletes All tuples in Movies
Drop Table Movies;		— Removes the table itself

Delete FROM Movies
WHERE "shortname" = 'Chef';

...

### Inserting Many Tuples

![](./Insert_&_Delete/pasted_image.png)


