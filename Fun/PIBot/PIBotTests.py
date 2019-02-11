'''
TODO: Here's a list of unit tests to create in order to prevent regression (use test subreddit):
6. Find a phone/email in a title, self, and comment
7. Check id_blacklist.txt for duplicates
Also: Connect to GitHub CI/CD.
'''


from Fun.PIBot import PIBot
import unittest
import os
import praw

class TestMatching(unittest.TestCase):


	def setUp(self):
		self.reddit = PIBot.bot_login()
		self.email_domains = ['@gmail.com', '@hotmail.com', '@live.ca', '@yahoo.com', '@yahoo.ca', '@aol.com', '@outlook.com']
		self.email_pattern = r"(\b(\w+(@\w+.[a-z]{0,3})))"
		self.phone_pattern = r"(?<!\w)[1 ]?[- ]?(?!800)\(?\d{3}\)?\s?[- ]?\d{3}[- ]?\d{3,4}(?!\d+?)"
		self.comment_blacklist = PIBot.create_local_cache("id_blacklist.txt")
		self.blacklist = [x.strip() for x in self.comment_blacklist.readlines()]

	def tearDown(self):
		os.remove("id_blacklist.txt")

	def test_login(self):
		self.assertIsNotNone(self.reddit)
		self.assertEqual(self.reddit.user.me(), praw.reddit.Reddit.redditor(self.reddit, name='PrivacyRobit'))

	def test_phone_match(self):
		test_comment = self.reddit.comment(id='e8wh8wk')
		results = PIBot.scan_text(test_comment, self.email_domains, self.email_pattern, self.phone_pattern, "comment")
		self.assertIsNotNone(results)
		self.assertEqual(test_comment.id, results)

	def test_email_match(self):
		test_comment = self.reddit.comment(id='eg5hbcc')
		results = PIBot.scan_text(test_comment, self.email_domains, self.email_pattern, self.phone_pattern, "comment")
		self.assertIsNotNone(results)
		self.assertEqual(test_comment.id, results)

	def test_submission_multiple(self):
		test_submission = self.reddit.submission(id='9thiao')
		results = PIBot.scan_text(test_submission, self.email_domains, self.email_pattern, self.phone_pattern, "submission")
		self.assertIsNotNone(results)
		self.assertEqual(results.id, test_submission.id)

	def test_comment_multiple(self):
		test_comment = self.reddit.comment(id='e8wcwlq')
		results = PIBot.scan_text(test_comment, self.email_domains, self.email_pattern, self.phone_pattern, "comment")
		self.assertIsNotNone(results)
		self.assertEqual(results.id, test_comment.id)

	def test_writeable_cache(self):
		self.assertTrue(os.access("id_blacklist.txt", os.W_OK))

if __name__ == '__main__':
	unittest.main()
