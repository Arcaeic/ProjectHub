import re
import praw


def bot_login():
	reddit = praw.Reddit('PIBot')
	print("Success! This program is logged in under " + str(reddit.user.me()) + "!")
	return reddit


def report(comment):
	return comment     # Placeholder


def scan_comment(comment, domains, email_pattern, phone_pattern, comment_cache):
	comment_cache.append(comment.id)                      # Add comment to cache, prevents spam
	email_regex = re.findall(email_pattern, comment.body)  # Check for email and phone matches
	phone_regex = re.findall(phone_pattern, comment.body)
	if email_regex:
		for match in range(0, len(email_regex)):
			if email_regex[match] in domains:
				print_match_text(email_regex[match], comment.author.name)
				report(comment)
	elif phone_regex:
		for match in range(0, len(phone_regex)):
			print_match_text(phone_regex[match], comment.author.name)
			report(comment)
	return


def print_match_text(data, author):
	print("\n   Found Match!")
	print("   Phone(s): " + data)
	print("   Author: " + author)


def skim(reddit):
	subreddit = reddit.subreddit('test')  # Placeholder, production will be set to 'all'
	email_domains = ['@gmail.com', '@hotmail.com', '@live.ca', '@yahoo.com', '@yahoo.ca', '@aol.com', '@outlook.com']
	comment_id_cache = []  # Container for previously visited comments
	email_pattern = r"(\b(\w+(@\w+.[a-z]{0,3})))"
	phone_pattern = r"(?<!\w)[1 ]?[- ]?(?!800)\(?\d{3}\)?\s?[- ]?\d{3}[- ]?\d{4}(?!\d+?)"
	for comment in subreddit.stream.comments():  # Look at each new comment as they are submitted
		if comment.id in comment_id_cache:  # Check if comment has already been visited, not likely
			print("\n   Found! Oh...I've already replied to this comment, skipping...")
			continue
		scan_comment(comment, email_domains, email_pattern, phone_pattern, comment_id_cache)


def main():
	reddit = bot_login()  # Initiate Reddit instance
	skim(reddit)  # Begin skimming for information


# End main
if __name__ == '__main__':
	main()
