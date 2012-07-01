import webapp2
import json
import urllib
import logging
import datetime

from google.appengine.ext import db
from google.appengine.api import users
from google.appengine.api import urlfetch
from google.appengine.api import search

import geo.geomodel
import geo.geotypes
import geo.geomath

from models import User
from models import Game


'''
Registers a new user
'''
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

'''
User creates a game - name (str), range (float), clue (string)
'''
class CreateGame(webapp2.RequestHandler):
    def post(self):
        user = users.get_current_user()
        
        if user == None:
            self.response.out.write(json.dumps({'error': 'Unauthenticated User'}))
            return
        
        #Create Game
        game = Game()
        game.name = self.request.get("name")
        game.players = []
        game.messages = []
        game.creator = user.key().id
        game.location = db.GeoPt(self.request.get('lat'), self.request.get('lon'))  ## Do we get current location? or is it sent by post?
        game.range = self.request.get('range')
        game.startTime = datetime.datetime.now()
        
        clue = Message()
        clue.text = self.request.get('clue')
        clue.user = user

        clue.time = datetime.time(datetime.datetime.now()) 
        clue.put()
        
        game.clue = clue.key()
        game.active = True
        game.put()

        #Notify users in the location
        
        #Return Game ID to UI
        return game.key().id

'''
Fetch Games in nearby area
Input: point of origin (lat/long) & range 
'''
class FetchGames(webapp2.RequestHandler):
    def get(self):
        #Get requester's current location
        point_of_origin = db.GeoPt(self.request.get("lat"), self.request.get("long"))
        range = self.request.get("range")
        query_keys = Game.all()
        query_keys.filter('active = ', True)
        active_games = db.get(query_keys)
        
        result = {}
        
        for g in active_games:
            distance = geo.geomath.distance(point_of_origin, g.location)
            if distance < g.range:
                result[g.key()] = g.location
        
        self.response.out.write(json.dumps(result))
                    
        '''
        index = search.Index(Game.location)
        query = "distance(Game.location, point_of_origin) < " + range
        try:
            results = index.search(query)
            for g in results:
                #process game ids
        except search.Error:
            self.response.out.write(search.Error.message)
        '''
        
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
class Messages(webapp2.RequestHandler):
    '''
    Message is posted to a game by a user
    '''
    def post(self, gameid):
        user = auth()

        if user == None:
            self.response.out.write(json.dumps({'error': 'No autheticated user'}))
            return
        gameid = int(gameid)
        message = json.loads(self.request.get("message"))
        # Handle message

    '''
    Messages are fetched by user using game id
    '''
    def get(self, gameid):
        user = auth()

        if user == None:
            self.response.out.write(json.dumps({'error': 'No autheticated user'}))
            return
        
        since = int(self.request.get("since"))
        q = db.GQLQuery("SELECT * FROM Message WHERE gameid= :1 AND time > :2", gameid, since)
        
        results = q.fetch()
        reply = {}
        for m in results:
            reply[m.key()] = {'img' : m.img,
                              'text': m.text
                              }
        return json.dumps(reply)
        # return messages

'''
When someone joins an ongoing game
'''
class Join(webapp2.RequestHandler):
    def post(self, gameid):
        user = auth()

        if user == None:
            self.response.out.write(json.dumps({'error': 'No autheticated user'}))
            return
        # add user to game

'''
Periodically update the users' Current location
'''
class Location(webapp2.RequestHandler):
    def post(self):
        user = auth()

        if user == None:
            self.response.out.write(json.dumps({'error': 'No autheticated user'}))
            return
        # Store user location



'''
Authenticates a user
'''
def auth():
    user = users.get_current_user()

    if user:
        player = User.gql("WHERE player = :1", user)
        return player.fetch(1)[0]
    else:
        return None

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
    ('/register', Register),
    ('/creategame', CreateGame),
    ('/startgame', StartGame),
    ('/fetchgames', FetchGames),
    webapp2.Route('/confirm/<messageid:[0-9]*>', handler=Message),
    webapp2.Route('/messsages/<gameid: [0-9]*>', handler=Messages),
    webapp2.Route('/join/<gameid: [0-9]*>', handler=Join),
    ('/location', Location),
    ], debug=True)