'''
TODO: Here's a list of unit tests to create in order to prevent regression (use test subreddit):
Also: Connect to GitHub CI/CD.
'''


from Fun.PIBot import PIBot
import unittest
import os
import praw


class TestRedditAndOS(unittest.TestCase):

	def setUp(self):
		self.reddit = PIBot.bot_login()

	def test_login(self):
		self.assertIsNotNone(self.reddit)
		self.assertEqual(self.reddit.user.me(), praw.reddit.Reddit.redditor(self.reddit, name='PrivacyRobit'))

	def test_writeable_cache(self):
		self.assertTrue(os.access("./", os.R_OK))
		self.assertTrue(os.access("./", os.W_OK))

		self.comment_blacklist = PIBot.create_local_cache("id_blacklist.txt")
		self.assertTrue(os.access("id_blacklist.txt", os.W_OK))
		os.remove("id_blacklist.txt")


class TestMatching(unittest.TestCase):

	def setUp(self):
		self.reddit = PIBot.bot_login()
		self.local_cache = PIBot.create_local_cache("id_blacklist.txt")
		self.blacklist = [x.strip() for x in self.local_cache.readlines()]
		self.email_domains = ['@gmail.com', '@hotmail.com', '@live.ca', '@yahoo.com', '@yahoo.ca', '@aol.com', '@outlook.com']
		self.email_pattern = r"(\b(\w+(@\w+.[a-z]{0,3})))"
		self.phone_pattern = r"(?<!\w)[1 ]?[- ]?(?!800)\(?\d{3}\)?\s?[- ]?\d{3}[- ]?\d{3,4}(?!\d+?)"

	def tearDown(self):
		os.remove("id_blacklist.txt")

	def test_phone_match(self):
		self.helper_scan_text(self.reddit, "comment", "e8wh8wk")

	def test_email_match_match(self):
		self.helper_scan_text(self.reddit, "comment", "eg5hbcc")

	def test_submission_title_phone(self):
		self.helper_scan_text(self.reddit, "submission", "9ti3g8")

	def test_submission_title_email(self):
		self.helper_scan_text(self.reddit, "submission", "apd4wu")

	def test_submission_multiple_phone_match(self):
		self.helper_scan_text(self.reddit, "submission", "9thiao")

	def test_comment_multiple_match(self):
		self.helper_scan_text(self.reddit, "comment", "e8wcwlq")

	def test_blacklist_no_duplicates_comment(self):
		self.helper_test_blacklist(self.local_cache, self.reddit.comment("e8wcwlq"))

	def test_blacklist_no_duplicates_comment_multi(self):
		self.helper_test_blacklist(self.local_cache, self.reddit.comment("eg7kdbg"))

	def test_blacklist_no_duplicates_submission(self):
		self.helper_test_blacklist(self.local_cache, self.reddit.submission("9thiao"))

	def helper_test_blacklist(self, file, reddit_object):

		PIBot.scan_id(reddit_object, file, self.blacklist, self.email_domains, self.email_pattern, self.phone_pattern)
		PIBot.scan_id(reddit_object, file, self.blacklist, self.email_domains, self.email_pattern, self.phone_pattern)
		PIBot.scan_id(reddit_object, file, self.blacklist, self.email_domains, self.email_pattern, self.phone_pattern)

		file.close()
		data = open("id_blacklist.txt", "r+").read()
		self.assertEqual(1, data.count(reddit_object.id))

	def helper_scan_text(self, reddit, instance_type, object_id):
		test_object = ""
		if instance_type is "submission":
			test_object = reddit.submission(id=object_id)
		elif instance_type is "comment":
			test_object = reddit.comment(id=object_id)

		results = PIBot.scan_text(test_object, self.email_domains, self.email_pattern, self.phone_pattern, instance_type)

		self.assertIsNotNone(results)
		if isinstance(results, str):
			self.assertEqual(test_object.id, results)
		else:
			self.assertEqual(test_object.id, results.id)


if __name__ == '__main__':
	unittest.main()
