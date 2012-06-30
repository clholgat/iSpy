import datetime
import webapp2

from google.appengine.ext import db
from google.appengine.api import users

class Game(db.Model):
    startTime = db.DateTimeProperty()
    players = db.ListPropety(db.Key)
    messages = db.ListProperty(db.Key)
    name = db.StringProperty()
    creator = db.ReferenceProperty(db.Key)

class User(db.Model):
    account = db.UserProperty()
    activeGame = db.ReferenceProperty()
    location = db.GeoPtProperty()
    # C2DM needed here

class Message(db.Model):
    time = db.TimeProperty()
    user = db.ReferenceProperty()
    img = db.LinkProperty()
    text = db.StringProperty()
    confirmed = db.BooleanProperty()