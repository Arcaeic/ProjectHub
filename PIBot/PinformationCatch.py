import re
import praw

def bot_login():
	reddit = praw.Reddit(client_id='n9gWxG8ZB5g1TQ',
						 client_secret='Krxt6RmXTSrD-WMWdUeOXJ4C87E',
						 password='***********',
						 user_agent='reddit:PrivacyBot V1.0 by /u/GreyLlama',
						 username='PrivacyRobit')
	print("Success! This program is logged in under " + str(reddit.user.me()) + "!")
	return reddit

def report(comment):
	return #Placeholder

def scan_comment(comment,domains, emailPattern, phonePattern, commentCache):
	commentCache.append(comment.id) #Add comment to cache, prevents spam
	emailMatch = re.match(emailPattern, comment.body) #Check for email and phone matches
	phoneMatch = re.match(phonePattern, comment.body)
	if emailMatch:
		for match in emailMatch.groups():
			if match in domains:
				print("\n   Found Match!")
				print("   Email(s): " + emailMatch.group(1))
				print("   Perp: " + comment.author.name)
				report(comment)
	elif phoneMatch:
		print("\n   Found Match!")
		print("   Phone(s): " + phoneMatch.group(0))
		print("   Perp: " + comment.author.name)
		report(comment)
	else:
		print("TEST: New comment! No match found!")
	return

def skim(reddit):
	subreddit = reddit.subreddit('test') #Placeholder, production will be set to 'all'
	emailDomains = ['@gmail.com','@hotmail.com','@live.ca','@yahoo.com',
					'@yahoo.ca','@aol.com','@outlook.com'] 		#Popular email domains
	commentIDCache = [] 						#Container for previously visited comments
	emailPattern = r"(\b(\w+(@\w+.[a-z]{0,3})))"
	phonePattern = r"(?<!\w)[1 ]?[- ]?(?!800)\(?\d{3}\)?\s?[- ]?\d{3}[- ]?\d{4}(?!\d+?)"
	for comment in subreddit.stream.comments(): 			#Look at each new comment as they are submitted
		if comment.id in commentIDCache:					#Check if comment has already been visited, not likely
			print("\n   Found! Oh...I've already replied to this comment, skipping...")
			continue
		scan_comment(comment, emailDomains, emailPattern, phonePattern, commentIDCache)

def main():
	reddit = bot_login() #Initiate Reddit instance
	skim(reddit)		 #Begin skimming for information
	#End main

if __name__ == '__main__':
	main()
