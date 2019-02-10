'''
TODO: Here's a list of unit tests to create in order to prevent regression (use test subreddit):
2. Can grab multiple matches from a comment
3. Can add information into a (test) log file
4. Can add comment IDs into a (test) local cache
5. Can log into the bot.
6. Find a phone/email in a title, self, and comment
7. Check id_blacklist.txt for duplicates
Also: Connect to GitHub CI/CD.
'''


from PIBot import PIBot
import unittest
import praw
from os import remove


class TestMatching(unittest.TestCase):

	def setUp(self):
		self.reddit = PIBot.bot_login()
		self.email_domains = ['@gmail.com', '@hotmail.com', '@live.ca', '@yahoo.com', '@yahoo.ca', '@aol.com', '@outlook.com']
		self.email_pattern = r"(\b(\w+(@\w+.[a-z]{0,3})))"
		self.phone_pattern = r"(?<!\w)[1 ]?[- ]?(?!800)\(?\d{3}\)?\s?[- ]?\d{3}[- ]?\d{4}(?!\d+?)"
		self.comment_blacklist = PIBot.create_local_cache("id_blacklist.txt")
		self.blacklist = [x.strip() for x in self.comment_blacklist.readlines()]

	def tearDown(self):
		remove("id_blacklist.txt")

	def test_phone_match(self):
		test_comment = self.reddit.comment(id='e8wh8wk')
		results = PIBot.scan_text(test_comment, self.email_domains, self.email_pattern, self.phone_pattern, "comment")
		self.assertIsNotNone(results)
		self.assertEqual(test_comment.id, results)

	def test_email_match(self):
		test_comment = self.reddit.comment(id='eg5hbcc')
		print(test_comment.body)
		results = PIBot.scan_text(test_comment, self.email_domains, self.email_pattern, self.phone_pattern, "comment")
		self.assertIsNotNone(results)
		self.assertEqual(test_comment.id, results)

if __name__ == '__main__':
	unittest.main()