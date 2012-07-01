import webapp2
import json
import urllib
import logging
import datetime

from google.appengine.ext import db
from google.appengine.api import users

import geo.geomodel
import geo.geotypes
import geo.geomath

from models import MyUser
from models import Game
from models import MyMessage


'''
Authenticates a user
'''
def auth():
    logging.debug("auth: ")    
    user = users.get_current_user()
    logging.debug(user)
    if user:
        logging.debug("auth: User logged in - ")
        #return user
        player = MyUser.gql("WHERE account = :1", user)
        return player.fetch(1)[0]
    else:
        logging.debug("auth: Cannot find a logged in user")
        return None
'''
Registers a new user
'''
class Register(webapp2.RequestHandler):
    def post(self):
        logging.debug("Register:")
        user = users.get_current_user()

        if user:
            player = MyUser.gql("WHERE account = :1", user).fetch(1)
            if len(player) == 0:
                player = MyUser()
                player.account = user
                player.deviceId = self.request.get("deviceId")
                player.put()
                logging.debug("Created new user")
            else:
                logging.debug("Existing user")
            self.response.out.write(json.dumps({'success': 'user registered'}))
        else:
            self.response.out.write(json.dumps({'error': 'No authenticated user'}))

'''
User creates a game - name (str), range (float), clue (string)
'''
class CreateGame(webapp2.RequestHandler):
    def post(self):
        logging.debug("CreateGame: ")
        user = auth()
        if user == None:
            logging.debug("CreateGame: Entered unauthenticated user!!")
            self.response.out.write(json.dumps({'error': 'Unauthenticated User'}))
            return
        logging.debug("CreateGame: Before creating Game Object")
        #Create Game
        game = Game()
        game.name = self.request.get("name")
        game.players = []
        game.messages = []
        game.creator = user.key().id()
        game.location = user.location
        game.range = float(self.request.get('range'))
        game.startTime = datetime.datetime.now()
        logging.debug("CreateGame: Before creating Clue Object")
        
        clue = MyMessage()
        clue.text = self.request.get('clue')
        clue.time = datetime.datetime.now() 
        clue.put()
        logging.debug("CreateGame: Clue Object Created")
        game.clue = clue.key().id()
        game.active = True
        game.put()
        logging.debug("Game object created")
        clue.gameid = game.key().id()
        clue.user = user
        clue.put()
        #Notify users in the location
        #Return Game ID to UI
        logging.debug(game.key().id())
        self.response.out.write(json.dumps({'gameid': game.key().id()}))

class FetchGames(webapp2.RequestHandler):
    def get(self):
        user = auth()
        if user == None:
            logging.debug("FetchGames: Entered unauthenticated user!!")
            self.response.out.write(json.dumps({'error': 'Unauthenticated User'}))
            return
        
        point_of_origin = user.location
        game_keys = Game.gql('WHERE active =  :1',True)
        active_games = game_keys.fetch(game_keys.count())
        
        result = {}
        
        for g in active_games:
            distance = geo.geomath.distance(point_of_origin, g.location)
            if distance < g.range:
                result[g.key()] = g.location
        self.response.out.write(json.dumps(result))

'''
User begins a game
'''
class StartGame(webapp2.RequestHandler):
    def post(self):
        user = auth()
        if user == None:
            self.response.out.write(json.dumps({'error': 'No autheticated user'}))
            return
        game = Game.gql("WHERE creator = :1", user)
        # handle notifications here


'''
When someone guesses the right option
'''
class Message(webapp2.RequestHandler):
    def post(self, messageid):
        user = auth()
        if user == None:
            self.response.out.write(json.dumps({'error': 'No autheticated user'}))
            return
        confirm = self.request.get("confirm");
        # confirm/deny guess

'''
Retrieves the list of messages
'''
class PostMessage(webapp2.RequestHandler):
    '''
    Message is posted to a game by a user
    '''
    def post(self, gameid):
        user = auth()
        if user == None:
            self.response.out.write(json.dumps({'error': 'No autheticated user'}))
            return
        gameid = int(gameid)
        text = self.request.get("text")
        bitmap = self.request.get("image")

        message = MyMessage()
        message.time = datetime.datetime.now()
        message.user = user
        message.gameid = gameid
        message.img = bitmap
        message.confirmed = False
        self.response.out.write(json.dumps({'success': 'sent message'}))
        # Handle message

class GetMessages(webapp2.RequestHandler):
    '''
    Messages are fetched by user using game id
    '''
    def get(self, gameid, since):
        user = auth()
        if user == None:
            self.response.out.write(json.dumps({'error': 'No autheticated user'}))
            return
        
        since = int(since)
        q = db.GQLQuery("SELECT * FROM Message WHERE gameid= :1 AND time > :2", gameid, since)
        
        results = q.fetch()
        reply = []
        for result in results:
            reply.append(result.toDict())
        return json.dumps(reply)

'''
When someone joins an ongoing game
Input: game id
'''
class Join(webapp2.RequestHandler):
    def post(self, gameid):
        user = auth()
        if user == None:
            logging.debug("CreateGame: Entered unauthenticated user!!")
            self.response.out.write(json.dumps({'error': 'No autheticated user'}))
            return
        
        # Check to see if the game is still active
        game = Game.get_by_id(int(gameid))
        if not game.active:
            self.response.out.write(json.dumps({'error': 'Game seems to have ended. Join some other game'}))
        else:
            game.players.append(user)
            game.put()
            user.activeGame = game
        
        # add user to game

'''
Periodically update the users' Current location
'''
class Location(webapp2.RequestHandler):
    def post(self):
        user = auth()
        if user == None:
            logging.debug("Location: User Authentication Error")
            self.response.out.write(json.dumps({'error': 'No autheticated user'}))
            return
        logging.debug("Location: User Authorized")
        # Store user location
        user.location = db.GeoPt(float(self.request.get('lat')), float(self.request.get('lon')))
        user.put()

'''
A Player sends a guess to the game initiator
'''
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
    
    #request = urllib.Request(url, data_encode, headers)
  #  response = urllib.urlopen(request)
  #  logging.debug(response.read())

app = webapp2.WSGIApplication([
    webapp2.Route('/register', handler=Register),
    ('/creategame', CreateGame),
    ('/startgame', StartGame),
    ('/fetchgames', FetchGames),
    webapp2.Route('/confirm/<messageid:[0-9]*>', handler=Message),
    webapp2.Route('/messages/<gameid:[0-9]*>/<since: [0-9]*>', handler=GetMessages),
    webapp2.Route('/messages/<gameid:[0-9]*>', handler=PostMessage),
    webapp2.Route('/join/<gameid:[0-9]*>', handler=Join),
    ('/location', Location),
    ], debug=True)