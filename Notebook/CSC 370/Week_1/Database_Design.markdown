# Database Design
Created Thursday 07 September 2017


* **Designing **a database:
	* What **information** the database must hold, and
	* What **relationships** are there among components of that information
* **Notation** for expressing designs: *Entity-Relationship (ER) model*


![](./Database_Design/pasted_image.png)

Elements of the E/R Model
-------------------------
![](./Database_Design/pasted_image001.png)

### Example
![](./Database_Design/pasted_image002.png)

### Multiplicity of Relationships
![](./Database_Design/pasted_image003.png)

### Back to the example
Why would some of the triples be invalid?

E.g
Carolco Pictures paid Arnold S. for Total recall
TriStar paid Sharon S. for Total Recall
Using "Owns" and "Stars-in" we will have the following triples:
(CP, TR, AS), (CP, TR, SS), (TS,TR,AS), (TS, TR, SS)
The second and third triple isn't valid
This is an example of a 3-way relationship

![](./Database_Design/pasted_image004.png)

![](./Database_Design/pasted_image005.png)

* A straight arrow means "0 or 1" (inclusive)
* A rounded arrow means "1" (exclusive)


