import webapp2
import json

class Register(webapp2.RequestHandler):
    def post(self):
        # Do something

class StartGame(webapp2.RequestHandler):
    def post(self):
        # Start game

class Message(webapp2.RequestHandler):
    def post(self, messageid):
        confirm = self.request.get("confirm");
        # confirm/deny guess

class Messages(webapp2.RequestHanlder):
    def post(self, gameid):
        gameid = int(gameid)
        message = json.loads(self.request.get("message"))
        # Handle message

    def get(self, gameid):
        since = int(self.request.get("since"))
        # return messages

class Join(webapp2.RequestHandler):
    def post(self, gameid):
        # add user to game

class Location(webapp2.RequestHandler):
    def post(self):
        # Store user location

class Register(webapp2.RequestHandler):
    def post(self):
        # set up user object