//               " node server " do terminala " ctrl c " zeby zabic proces;  mongodbhttp://localhost:8000/note
//curl -X POST -H "Content-Type: application/json" -d "{\"title\": \"note1\", \"content\": \"Sample note content\"}" http://localhost:8000/notepost
//curl -X DELETE http://localhost:8000/note/6484d9ad074c124829712401

//importuje package (lokalne)
const path = require("path");
const express = require("express");
const mongoose = require("mongoose");
const cookieParser = require("cookie-parser");
const bodyParser = require("body-parser");
const methodOverride = require("method-override");
const jwt = require("jsonwebtoken");
const bcrypt = require("bcrypt");    

const Note = require("./models/note.js"); //schema z pliku note.js
const User = require("./models/user.js"); //do logowania
const auth = require("./middleware/auth"); //autoryzacja


const app = express();

app.use(express.static(path.join(__dirname, "public")));
app.use( bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json()); //powinny pozwolic wyciagnac dane do req.body
require("dotenv").config();         //jwt
app.use(express.json());            //jwt
app.use(cookieParser());

const ejs = require('ejs');
const req = require("express/lib/request.js");
app.set('view engine', 'ejs'); //dla pliku ejs   
//app.use(express.static('public'));     

app.use(methodOverride('_method')); //dla guzikow z pliku.ejs


//do mangodb atlas
const uri = "mongodb+srv://www2023:Www2023@cluster0www.d6l9ful.mongodb.net/?retryWrites=true&w=majority";
//laczenie do mangodb
async function connect() { //async bo nie wiadomo jak długo zajmnie łączenie
    try{
        await mongoose.connect(uri);
        console.log("Connect to MongoDB");
    }
    catch(err){
        console.error("Error, while connecting to MongoDB:", err); //wyswietl błąd na konsoli
    }
}
connect(); //wywołuje tą funkcje ^

//laduje css
app.get('/public/style.css', (req, res) => {
    res.sendFile(path.join(__dirname, 'public', 'css', 'style.css'));
});

tokenKey = process.env.TOKEN_KEY;


//rejestracja
app.post("/register", async(req, res) => {
    console.log('Rejestracja');
    
    res.clearCookie("token"); // Replace "token" with the name of your cookie
    try {
        //z tego co wpisał user
        const { first_name, last_name, email, password } = req.body;
        if (!(email && password && first_name && last_name)) {
            res.status(400).send("All input is required");
            console.log('Brakuje danych');
        }

        // czy juz jest taki uzytkownik, po mailu
        const oldUser = await User.findOne({ email });
        if (oldUser) {
            console.log('juz istnial taki uzytkownik');
            return res.status(409).send("User Already Exist. Please Login");
        }
        //koduje haslo
        encryptedPassword = await bcrypt.hash(password, 10);
        // Uzytkownicy twozrenie
        const user = await User.create({
            first_name,
            last_name,
            email: email.toLowerCase(), // email w malych literach
            password: encryptedPassword,
        });
        // tworzy token
        const token = jwt.sign(
            { 
                user_id: user._id, 
                email 
            },
            tokenKey,
            {
                expiresIn: "2h",
            }
        );
        // zapisuje user token
        user.token = token;
        console.log('nowy uzytkownik', user);
        //res.status(201).json(user);
        res.redirect("/login");
    } catch (err) {
        console.log(err);
    }
});

//logowanie
app.post("/login", async (req, res) => {
    try {
        const { email, password } = req.body;
        if (!(email && password)) {
            res.status(400).send("All input is required");
            console.log('Brakuje danych');
        }
        //czy jest takie konto
        const user = await User.findOne({ email });
        
        if (user && (await bcrypt.compare(password, user.password))) {
            const token = jwt.sign(
            { user_id: user._id, email },
            tokenKey,
            {
                expiresIn: "2h",
            }
            );
            user.token = token;
            res.cookie("token", token, { maxAge: 2 * 60 * 60 * 1000 }); // na 2h tylko

            console.log('weszlo');
            //res.header("x-access-token", token);
            res.redirect('/');
            //res.status(200).json(user);
        }
        else{
            console.log('nie weszlo');
            res.status(400).send("Wrong password or email");
        }
    } 
    catch (err) {
        console.log(err);
    }
});
//curl -X POST -H "Content-Type: application/json" -d "{\"email\": \"test@gmail.com\", \"password\": \"haslo\"}" http://localhost:8000/login


//przy wejściu   ( http://127.0.0.1:8000 )
app.get('/', auth, function(req,res){
//app.get('/', function(req,res){
    console.log('Visited app`s mainpage');
    res.sendFile(path.join(__dirname+'/views/notee.html')); //jak ten html
});

//zwraca liste wszystkich notatek po kliknieciu przycisku
app.get('/note', auth, async (req, res) => {
//app.get('/note', async (req, res) => {
    console.log("GET /note");
    try{
        const notes = await Note.find({}); // Pobierz wszystkie notatki z bazy danych
        res.render('showingNotes', {notes}); //plik showingNotes.ejs
    }
    catch (err) {
        console.error('Error during loading notes:', err);
        res.status(500).send('Server error');        
    }
});

app.get('/note/:id', auth, async (req, res) => {
//app.get('/note/:id', async (req, res) => {

    console.log("GET /note/:id");

    const noteId = req.params.id;
    try {
        const note = await Note.findById(noteId); // Pobierz notatkę o podanym ID z bazy danych
        if (!note) {
            res.status(404).send('Notatka nie znaleziona');
            return;
        }
        res.render('showOneNote', {note});
        //zostawione w formacie json
        //res.json(note); // Odpowiedz z notatką w formacie JSON 
    } 
    catch (err) {
        console.error('Error podczas pobierania notatki:', err);
        res.status(500).send('Server error');
    }
});
 
//zastepuje router bez hash
//const Note = require("./models/note.js") //juz jest
app.post('/note', auth, (req, res) => {
//app.post('/note', (req, res) => {
    console.log("POST /note");

    //bo title jako required
    if (!req.body.title) {
        return res.status(400).send('Title is required');
    }
    const notatka = new Note({ //nwoa notatka z modelu notatki
        title: req.body.title,
        content: req.body.content
    });
    //nwm czy konieczne - hashuje(koduje) ten fragment
    notatka.save()
        .then(data => {
            console.log("Created new note", data);
            res.send("POST /note - new note created");
        })
        .catch(error => {
            console.error('Error while creating', error);
            res.send("There was an error while creating new note");

        });
    //module.exports = router;
});

app.put('/note/:id', auth, async (req, res) => {
//app.put('/note/:id', async (req, res) => {
    console.log("PUT /note/:id");
    const noteId = req.params.id;
    try{
        const updateNote = await Note.findByIdAndUpdate(noteId, req.body, { new: true }); 
        if (!updateNote) {
            res.status(404).send('Note wasnt found');
            console.log('No note by id');
            return;
        }        
        //res.status(204).send('DELETE /note/:id - Note is deleted');
        console.log('Updated note');
        res.redirect('/note');
    }
    catch (err) {
        console.error('Error during deleting note:', err);
        res.status(500).send('Server error');        
    }
});

app.delete('/note/:id', auth, async (req, res) => {
//app.delete('/note/:id', async (req, res) => {
    console.log("DELETE /note/:id");
    const noteId = req.params.id;
    try{
        const delNote = await Note.findByIdAndDelete(noteId); // Pobierz wszystkie notatki z bazy danych
        if (!delNote) {
            res.status(404).send('There was no note');
            console.log('No note by id');
            return;
        }        
        //res.status(204).send('DELETE /note/:id - Note is deleted');
        console.log('Deleted note');
        res.redirect('/note');
    }
    catch (err) {
        console.error('Error during deleting note:', err);
        res.status(500).send('Server error');        
    }
});

//curl -X POST -H "Content-Type: application/json" -H "x-access-token: <>" http://localhost:8000/welcome
app.get("/welcome", auth, (req, res) => {
    console.log('elo elo welcome');
    res.status(200).send("Welcome 🙌 ");
});


app.get('/login', (req, res) => {
    console.log('logownie');
    res.render('login');
});

// Route to render the registration form
app.get('/register', (req, res) => {
    console.log('rejestr');
    res.render('register');
});


//apka na porcie '8000'
const port = 8000;
app.listen(port, () => {
    console.log("Server started on port ",port); //ctrl c  -zeby zabic proces

});