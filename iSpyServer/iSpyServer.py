import webapp2
import json
import datetime
import urllib
import logging

from google.appengine.ext import users

class Register(webapp2.RequestHandler):
    def post(self):
        user = users.get_current_user()

        if user:
            player = User()
            player.account = user
            player.deviceId = int(self.request.get("deviceId"))
            player.put()
        else:
            self.repsonse.out.write(json.dumps({'error': 'No authenticated user'}))

        self.response.out.write(json.dumps({'success': 'user registered'}))

class StartGame(webapp2.RequestHandler):
    def post(self):
        user = auth()

        if user == None:
            self.response.out.write(json.dumps({'error': 'No autheticated user'}))
            return

        game = Game.gql("WHERE creator = :1", user)
        # handle notifications here


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

def auth():
    user = users.get_current_user()

    if user:
        player = Users.gql("WHERE player = :1", user)
        return player.fetch(1)[0]

def sendMessage(player, message):
    # logging.debug(message.text)
    url = "https://android.googleapis.com/gcm/send"
    headers = {
    'Content-Type': 'application/json',
    'Authorization': 'key=AIzaSyAyHVqjOn9VZOFqFDYRxO9y168gSTpVmfc'
    }
    data = {
        'registration_id': player.registrationId,
        'data': message.txt
    }

    data_encode = urllib.urlencode(data)
    request = urllib.Request(url, data_encode, headers)
    response = urllib.urlopen(request)
    logging.debug(response.read())

app = webapp2.WSGIApplication([
    ('/register', Register),
    ('/startgame', StartGame),
    webapp2.Route('/message/<messageid:[0-9]*>', handler=Message),
    webapp2.Route('/messsages/<gameid: [0-9]*>', handler=Messages),
    webapp2.Route('/join/<gameid: [0-9]*>', handler=Join),
    ('/location', Location),
    ('/register', Register),
    ], debug=True)