import webapp2
import json

from google.appengine.ext import users

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

def auth():
    user = users.get_current_user()

    if user:
        # we'll get back to this

def sendMessage(player, message):
    logging.debug(message.text)
    url = "https://android.clients.google.com/c2dm/send"
    payload = {}
    fields = {
        'registration_id': player.registrationId,
        'collapse_key': player.player.nickname(),
        'data.payload': message.text,
        'Authorization': 'GoogleLogin auth=%s'%getAuthKey()
    }

    form_data = urllib.urlencode(fields)
    logging.debug(form_data)
    result = urlfetch.fetch(url=url, payload=form_data, method=urlfetch.POST, headers={'Content-Type': 'application/x-www-form-urlencoded', 'Authorization': 'GoogleLogin auth=%s'%getAuthKey()})
    logging.debug(result.content)

token = None
def getAuthKey():
    global token
    if token != None:
        return token
    config = ConfigParser.ConfigParser()
    config.read('login.config')
    mk = __name__+".authkey"
    form_fields = {
        "accountType": config.get('login', 'accountType'),
        "Email": config.get('login', 'Email'),
        "Passwd": config.get('login', 'Passwd'),
        "service": config.get('login', 'service'), ## ( c2dm_service = "ac2dm" )
        "source": config.get('login', 'source'),
    }
    form_data = urllib.urlencode(form_fields)
    result = urlfetch.fetch(url='https://www.google.com/accounts/ClientLogin',
                            payload=form_data,
                            method=urlfetch.POST,
                            headers={'Content-Type': 'application/x-www-form-urlencoded'})
    fields=result.content.split("\n")
    token = fields[2].split('=')[1]
    return token

app = webapp2.WSGIApplication([
    ('/register', Register),
    ('/startgame', StartGame),
    webapp2.Route('/message/<messageid:[0-9]*>', handler=Message),
    webapp2.Route('/messsages/<gameid: [0-9]*>', handler=Messages),
    webapp2.Route('/join/<gameid: [0-9]*>', handler=Join),
    ('/location', Location),
    ('/register', Register),
    ], debug=True)