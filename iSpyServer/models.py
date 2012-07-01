import datetime
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
    clue = db.Key()                         ## The initial clue that starts a game
    active = db.BooleanProperty()

class User(db.Model):
    account = db.UserProperty()
    activeGame = db.ReferenceProperty(Game)
    location = db.GeoPtProperty()           # Users most recent location
    deviceId = db.StringProperty()

class Message(db.Model):
    time = db.TimeProperty()
    user = db.ReferenceProperty(User)
    gameid = db.ReferenceProperty(Game)
    img = db.LinkProperty()
    text = db.StringProperty()
    confirmed = db.BooleanProperty()