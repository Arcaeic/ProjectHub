import re
import praw

# Global Config
blacklist_file = "comment_blacklist.txt"


def bot_login():
	reddit = praw.Reddit('PIBot')
	print("Success! This program is logged in under " + str(reddit.user.me()) + "!")
	return reddit


def report(comment):
	return comment     # Placeholder


def scan_text(text, domains, email_pattern, phone_pattern):

	email_regex = ""
	phone_regex = ""

	# Check if object is a Comment or Submission
	if isinstance(text, praw.reddit.models.Comment):
		email_regex = re.findall(email_pattern, text.body)
		phone_regex = re.findall(phone_pattern, text.body)
	elif isinstance(text, praw.reddit.models.Submission):

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


def add_comment_to_blacklist(cid):

	blacklist = open(blacklist_file, "a")
	blacklist.write(cid + "\n")
	blacklist.close()

	return


def skim(reddit):

	subreddit = reddit.subreddit('Readet')  # Placeholder, production will be set to 'all'
	email_domains = ['@gmail.com', '@hotmail.com', '@live.ca', '@yahoo.com', '@yahoo.ca', '@aol.com', '@outlook.com']
	email_pattern = r"(\b(\w+(@\w+.[a-z]{0,3})))"
	phone_pattern = r"(?<!\w)[1 ]?[- ]?(?!800)\(?\d{3}\)?\s?[- ]?\d{3}[- ]?\d{4}(?!\d+?)"

	comment_blacklist = open(blacklist_file, "r")
	stripped_blacklist = [x.strip() for x in comment_blacklist.readlines()]
	comment_blacklist.close()

	for comment in subreddit.stream.comments():  # Look at each new comment as they are submitted
		if comment.id not in stripped_blacklist:  # Check if comment has already been visited.
			scan_text(comment, email_domains, email_pattern, phone_pattern)
			add_comment_to_blacklist(comment.id)
			stripped_blacklist.append(comment.id)
		else:
			print("\nFound! Oh...I've already found comment " + comment.id + ", skipping...")

		if comment.submission.id not in stripped_blacklist:
			scan_text(comment.submission, email_domains, email_pattern, phone_pattern)
			add_comment_to_blacklist(comment.submission.id)
			stripped_blacklist.append(comment.submission.id)
		else:
			print("\nFound! Oh...I've already found submission " + comment.submission.id + ", skipping...")


def main():
	reddit = bot_login()
	skim(reddit)


# End main
if __name__ == '__main__':
	main()
