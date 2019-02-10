import re
import praw
import os.path


def create_local_cache(filename):
	if os.path.isfile(filename):
		return open(filename, "r+")
	else:
		return open(filename, "w+")


''' bot_login()
:desc       Refers to praw.ini and logs into the designated account.
:returns    An instance of reddit
'''


def bot_login():
	reddit = praw.Reddit('PIBot')
	print("Success! This program is logged in under " + str(reddit.user.me()) + "!")
	return reddit


# TODO
def report(comment):
	return comment


''' scan_text(text, domains, email_pattern, phone_pattern, instance_type)
:param text             The reddit instance
:param domains          The email domains to filter against
:param email_pattern    The regular expression pattern used to catch emails
:param phone_pattern    The regular expression pattern used to catch emails
:param instance_type    A string determining the type of Reddit instance
:desc Scans the Reddit instance, then iterates through all the matches of either Emails and Phone Numbers
'''


def scan_text(text, domains, email_pattern, phone_pattern, instance_type):

	email_regex = ""
	phone_regex = ""
	match_found = 0

	# Check if object is a Comment or Submission
	if instance_type is "comment":
		email_regex = re.findall(email_pattern, text.body)
		phone_regex = re.findall(phone_pattern, text.body)
	elif instance_type is "submission":
		# Check if Submission is a self-post or link
		if text.selftext:
			email_regex = re.findall(email_pattern, text.selftext)
			phone_regex = re.findall(phone_pattern, text.selftext)
		else:
			email_regex = re.findall(email_pattern, text.title)
			phone_regex = re.findall(phone_pattern, text.title)

	# Check for pattern-matching
	if email_regex:
		for match in range(0, len(email_regex)):
			length_match_list = len(email_regex[match])
			if email_regex[match][length_match_list - 1] in domains:
				print_match_text(email_regex[match][0], text)
				report(text)
				match_found = 1

	elif phone_regex:
		for match in range(0, len(phone_regex)):
			print_match_text(phone_regex[match], text)
			report(text)
			match_found = 1

	return text.id if match_found is 1 else None

''' print_match_text(pi, text)
:param pi   The Personal Information (PI) skimmed from a Reddit instance
:param text The Reddit instance - used for getting the author name
'''


def print_match_text(pi, text):
	print("\nFound Match!")
	print("Phone / E-Mail: " + pi)
	if text.author:
		print("Author: " + text.author.name)


''' scan_id(reddit_instance, local_cache, blacklist, email_domains, email_pattern, phone_pattern)
:param reddit_instance  A Comment or Submission instance
:param local_cache      The file containing already traversed instance IDs
:param blacklist        The stripped blacklist
:param email_domains    A list of email domains to filter against
:param email_pattern    The regex pattern used to catch email addresses
:param phone_pattern    The regex pattern used to catch phone numbers
:desc   Checks if the Reddit instance is a comment or submission, then scans the instance for emails for phone numbers
		The instance ID is added to the blacklist file and list.
'''


def scan_id(reddit_instance, local_cache, blacklist, email_domains, email_pattern, phone_pattern):

	if isinstance(reddit_instance, praw.reddit.models.Comment):
		instance_type = "comment"
	else:
		instance_type = "submission"

	if reddit_instance.id not in blacklist:
		scan_text(reddit_instance, email_domains, email_pattern, phone_pattern, instance_type)
		local_cache.write(reddit_instance.id + "\n")
		blacklist.append(reddit_instance.id)
	else:
		print("\nFound! Oh...I've already found " + instance_type + " " + reddit_instance.id + ", skipping...")


''' skim(reddit, subreddit)
:param reddit       The bot, that is currently connected to Reddit
:param subreddit    A string designating the target subreddit.
:desc               Opens the blacklist file then loads the IDs into a list, then scans comments and submissions
'''


def skim(reddit, subreddit):

	subreddit = reddit.subreddit(subreddit)  # Placeholder, production will be set to 'all'
	email_domains = ['@gmail.com', '@hotmail.com', '@live.ca', '@yahoo.com', '@yahoo.ca', '@aol.com', '@outlook.com']
	email_pattern = r"(\b(\w+(@\w+.[a-z]{0,3})))"
	phone_pattern = r"(?<!\w)[1 ]?[- ]?(?!800)\(?\d{3}\)?\s?[- ]?\d{3}[- ]?\d{4}(?!\d+?)"

	comment_blacklist = create_local_cache("id_blacklist.txt")
	blacklist = [x.strip() for x in comment_blacklist.readlines()]

	for comment in subreddit.stream.comments():  # Look at each new comment as they are submitted, infinite
		scan_id(comment, comment_blacklist, blacklist, email_domains, email_pattern, phone_pattern)
		scan_id(comment.submission, comment_blacklist, blacklist, email_domains, email_pattern, phone_pattern)

	comment_blacklist.close()


# Added for better accessibility at command line
if __name__ == '__main__':
	reddit = bot_login()
	skim(reddit, "Readet")
