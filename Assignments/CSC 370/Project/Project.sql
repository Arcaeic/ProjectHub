--Q1: Determining the number of releases per country
SELECT country.name, COUNT(release.name) AS "Releases"
FROM country, release
WHERE country.id = release.country
GROUP BY country.name
ORDER BY "Releases" DESC;

--Q2: Determining the average track count per country.
SELECT country.name, AVG(tracklist.track_count) AS "Track Count"
FROM country, tracklist, release, medium
WHERE country.id = release.id AND release.id = medium.id AND medium.tracklist = tracklist.id
GROUP BY country.name
ORDER BY "Track Count" DESC;

--Q3: Who wrote the longest track?
SELECT artist_name.name, artist.country, track_name.name, MAX(track.length)
FROM artist_name, TRACK, track_name, r_artist_track, artist
WHERE artist_name.id = artist.id AND artist.name = r_artist_track.entity0 AND r_artist_track.entity1 = track.id AND track.id = track_name.id;

--Q4: The amount of releases per media format
SELECT medium_format.name, medium_format.year, SUM(MEDIUM.FORMAT) AS "Count"
FROM medium_format, medium, release, release_name
WHERE RELEASE.ID = MEDIUM.RELEASE AND medium.format = medium_format.id AND release_name.id = release.name
GROUP BY medium.format
ORDER BY "Count" DESC;

--Q5: Display all releases including the word 'The' in the title
SELECT artist_name.name AS "Artist", release_name.name AS "Release", release.date_year AS "Year"
FROM r_artist_release, artist, release, release_name, artist_name
WHERE artist_name.id = artist.name AND artist.id = r_artist_release.entity0 AND r_artist_release.entity1 = release.id AND release.name = release_name.id AND release_name.name LIKE '%The%'
;

--Q6: Which bands have composed the most tracks?
SELECT artist_name.name, COUNT(track_name.name) AS "Count"
FROM artist_name, artist, r_artist_track, track, track_name
WHERE artist_name.id = artist.name AND artist.id = r_artist_track.entity0 AND r_artist_track.entity1 = track.id AND track.name = track_name.id
GROUP BY artist_name.name
ORDER BY Count DESC
LIMIT 100;

--Q7: Listing the Number of Men and Women artists in each country
SELECT t1."c1", "Men", "Women"
FROM
	(SELECT country.name AS "c1", COUNT(gender.name) AS "Men"
	FROM gender, artist, country
	WHERE gender.id = artist.gender AND artist.country = country.id AND gender.name = 'Male'
	GROUP BY country.name) t1
JOIN
	(SELECT country.name AS "c2", COUNT(gender.name) AS "Women"
	FROM gender, artist, country
	WHERE gender.id = artist.gender AND artist.country = country.id AND gender.name = 'Female'
	GROUP BY country.name) t2
ON t2."c2" = t1."c1"
GROUP BY t1."c1";

--Q8: Which releases had the most tracks?
SELECT release_name.name, track_count
FROM release_name, release, medium, tracklist
WHERE release_name.id = release.name AND medium.release = release.id AND medium.tracklist = tracklist.id
ORDER BY track_count DESC
LIMIT 30;

--Q9: What is the shortest track?
SELECT track_name.name, MIN(track."length")
FROM track, track_name
WHERE track.name = track_name.id;

--Q10: How many solo/group artists exist per country?
SELECT t1."c1", "Person", "Group"
FROM
	(SELECT country.name AS "c1", COUNT(artist_type.name) AS "Person"
	FROM artist_type, artist, country
	WHERE artist_type.id = artist.type AND artist.country = country.id AND artist_type.name = 'Person'
	GROUP BY country.name) t1
JOIN
	(SELECT country.name AS "c2", COUNT(artist_type.name) AS "Group"
	FROM artist_type, artist, country
	WHERE artist_type.id = artist.type AND artist.country = country.id AND artist_type.name = 'Group'
	GROUP BY country.name) t2
ON t2."c2" = t1."c1"
GROUP BY t1."c1";