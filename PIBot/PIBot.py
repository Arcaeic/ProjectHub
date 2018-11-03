import re
import praw

# Global Config
blacklist_file = "comment_blacklist.txt"


def bot_login():
	reddit = praw.Reddit('PIBot')
	print("Success! This program is logged in under " + str(reddit.user.me()) + "!")
	return reddit


def report(comment):
	return comment     # TODO


def scan_text(text, domains, email_pattern, phone_pattern, instance_type):

	email_regex = ""
	phone_regex = ""

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
			if email_regex[match] in domains:
				print_match_text(email_regex[match], text.author.name)
				report(text)
	elif phone_regex:
		for match in range(0, len(phone_regex)):
			print_match_text(phone_regex[match], text)
			report(text)
	return


def print_match_text(pi, text):
	print("\nFound Match!")
	print("Phone / E-Mail: " + pi)
	if text.author:
		print("Author: " + text.author.name)
	return


def add_id_to_blacklist(cid):

	blacklist = open(blacklist_file, "a")
	blacklist.write(cid + "\n")
	blacklist.close()
	return


def scan_id(reddit_instance, blacklist, email_domains, email_pattern, phone_pattern):

	instance_type = ""
	if isinstance(reddit_instance, praw.reddit.models.Comment):
		instance_type = "comment"
	else:
		instance_type = "submission"

	if reddit_instance.id not in blacklist:
		scan_text(reddit_instance, email_domains, email_pattern, phone_pattern, instance_type)
		add_id_to_blacklist(reddit_instance.id)
		blacklist.append(reddit_instance.id)
	else:
		print("\nFound! Oh...I've already found " + instance_type + " " + reddit_instance.id + ", skipping...")


def skim(reddit):

	subreddit = reddit.subreddit('Readet')  # Placeholder, production will be set to 'all'
	email_domains = ['@gmail.com', '@hotmail.com', '@live.ca', '@yahoo.com', '@yahoo.ca', '@aol.com', '@outlook.com']
	email_pattern = r"(\b(\w+(@\w+.[a-z]{0,3})))"
	phone_pattern = r"(?<!\w)[1 ]?[- ]?(?!800)\(?\d{3}\)?\s?[- ]?\d{3}[- ]?\d{4}(?!\d+?)"

	comment_blacklist = open(blacklist_file, "r")
	blacklist = [x.strip() for x in comment_blacklist.readlines()]
	comment_blacklist.close()

	for comment in subreddit.stream.comments():  # Look at each new comment as they are submitted
		scan_id(comment, blacklist, email_domains, email_pattern, phone_pattern)
		scan_id(comment.submission, blacklist, email_domains, email_pattern, phone_pattern)


def main():
	reddit = bot_login()
	skim(reddit)


# End main
if __name__ == '__main__':
	main()
