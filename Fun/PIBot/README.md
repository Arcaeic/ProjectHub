__PIBot__ (or __PersonalInformationBot__) is a handy reddit bot written in Python using 
[Python's Reddit API Wrapper (PRAW)](https://praw.readthedocs.io/en/latest/). __PIBot__ searches through 
a subreddit, whether that may be 'all' or a specific subreddit, and is able to find users sharing 
personal email addresses, as well as non 1-800 phone numbers.

In order to find emails and phone numbers, this program uses [Regular Expressions](https://regexone.com/) 
to identify certain formats of phone numbers and popular email domains. As a result, the bot can detect 
multiple national phone number formats (albiet not all).

## Features
* (__Done__) Detect Emails using popular domains (gmail, hotmail, aol, yahoo, etc)
	* (__Done__)Make sure it doesnt detect private email domains
	* (__Done__) Match, then check if the domain list contains the domain of the email
	* (__Done__) Use a list of popular domain names to avoid flagging private emails
	* (__Done__) Use regexp and search for multiple formats of telephone number.
		* (__Done__) FIND ONLY NON 1-800 NUMBERS!
* (__Development__) Report posts
	* If it detects, give 5 minutes grace period before reporting,
	  check if post still is still flagged/exists before report.
		*	Comment.report(reason)
* (__Development__) Report, filter and footer!
	* Make a footer for the bot, including an opt-out function
    * Filter to prevent repeat reports (store matches in local file)
    * Finds a moderator to report to
	
## Suggestions
* (__Development__) Look for other popular email domains
    * Move email domains to a config file, load into list
* (__TBA__) Only report phone number or email address once
    * Move information into a local file, add to .gitignore, and encrypt the file
* (__Development__) Use a .txt file to keep a list of comment.id already replied to, 
then when script is executed, read in existing comment.id
	* This will prevent the bot from spamming users when the program is terminated.
	* Move comment ID to a config file, store into file and load into list on start-up
* (__Deprecated__) Iff someone PMs the bot with a certain format followed by an email or number, 
the number is blacklisted from being detected. Use a link to help human.

#### Preventing PIBot from being banned Reddit-wide
* (__TBA__) Give mods the ability to blacklist the bot from their subreddits
* (__TBA__) Make sure PIBot only comments when asked, if at all.
	* (__Done__) No comments! Interacts with mods via report msg / modmail.
	* (__Done__) Only reports, making PIBot more for the mods than users.
	* (__TBA__) Instead of replying to each comment, email the person explaining to them that their 
	information has been compromised.


## Todo
1. Finish Report
    * Check subreddit blacklist before reporting
    * Find a moderator of the current subreddit: Subreddit.message(subject, message)
    * Prepare a template, then have the bot send a message to the moderator (with footer)
    * Filter match in local file to prevent spam
2. Finish Opt-Out / Subreddit Blacklist
    * Prepare a footer template, include a link to the PM page with 'User' field filled with 'PrivacyRobit'
    * Moderator sends the name of the subreddit in the PM (without r/, /r/, etc.)
    * Checks if the user is a moderator of the provided subreddit, then add subreddit name to a local file
3. Finish Filter
    * Check for a listed match before reporting
    * Store (strip special characters from match for less false-negatives)
    * Maintain a list of tuples (subreddit:match), store in a dictionary+local file storage or DB
2. See: "Preventing PIBot from being banned Reddit-wide"
3. Release (Pull Request)
4. Work on To do, report errors to GitHub, fix those as well
