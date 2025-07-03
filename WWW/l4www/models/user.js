//curl -X POST -H "Content-Type: application/json" -d "{\"first_name\": \"Idris\", \"last_name\": \"Jakis\", \"email\": \"dris@gmail.com\", \"password\": \"123\}" http://localhost:8000/register
const mongoose = require("mongoose");

const userSchema = new mongoose.Schema({
    first_name: { 
        type: String, 
        default: null 
    },
    last_name: { 
        type: String, 
        default: null 
    },
    email: { 
        type: String, 
        unique: true 
    },
    password: { 
        type: String 
    },
    token: { 
        type: String 
    },
});

module.exports = mongoose.model("user", userSchema);