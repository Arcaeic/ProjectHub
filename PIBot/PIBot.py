import re
import praw

#Global Config
blacklist_file = "comment_blacklist.txt"

def bot_login():
	reddit = praw.Reddit('PIBot')
	print("Success! This program is logged in under " + str(reddit.user.me()) + "!")
	return reddit


def report(comment):
	return comment     # Placeholder


def scan_comment(comment, domains, email_pattern, phone_pattern):
	email_regex = re.findall(email_pattern, comment.body)  # Check for email and phone matches
	phone_regex = re.findall(phone_pattern, comment.body)
	if email_regex:
		for match in range(0, len(email_regex)):
			if email_regex[match] in domains:
				print_match_text(email_regex[match], comment.author.name)
				report(comment)
	elif phone_regex:
		for match in range(0, len(phone_regex)):
			print_match_text(phone_regex[match], comment)
			report(comment)
	return


def print_match_text(data, comment):
	print("\nFound Match!")
	print("Phone(s): " + data)
	if comment.author:
		print("Author: " + comment.author.name)
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
		if comment.id in stripped_blacklist:  # Check if comment has already been visited, not likely
			print("\nFound! Oh...I've already found comment " + comment.id + ", skipping...")
			continue
		scan_comment(comment, email_domains, email_pattern, phone_pattern)
		add_comment_to_blacklist(comment.id)
		stripped_blacklist.append(comment.id)

def main():
	reddit = bot_login()  # Initiate Reddit instance
	skim(reddit)  # Begin skimming for information


# End main
if __name__ == '__main__':
	main()