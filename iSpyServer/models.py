import datetime
import time
import webapp2

from google.appengine.ext import db
from google.appengine.api import users

class Game(db.Model):
    startTime = db.DateTimeProperty()
    players = db.ListProperty(db.Key)
    messages = db.ListProperty(db.Key)
    name = db.StringProperty()
    creator = db.IntegerProperty()
    location = db.GeoPtProperty()           #Location around which the game is centered
    range = db.FloatProperty()              #Area around the center of the game where players can join
    clue = db.IntegerProperty()                         ## The initial clue that starts a game
    active = db.BooleanProperty()

class MyUser(db.Model):
    account = db.UserProperty()
    activeGame = db.ReferenceProperty(Game)
    location = db.GeoPtProperty()           # Users most recent location
    deviceId = db.StringProperty()

class MyMessage(db.Model):
    time = db.DateTimeProperty()
    user = db.ReferenceProperty(MyUser)
    gameid = db.IntegerProperty()
    img = db.TextProperty()
    text = db.StringProperty()
    confirmed = db.BooleanProperty()

    def toDict(self):
        return {'messageId': int(self.key().id()), 'time': time.mktime(self.time.timetuple()), 'user': int(self.user.key().id()), 'username': str(self.user.account), 
            'img': self.img, 'text': self.text, 'confirmed': self.confirmed}