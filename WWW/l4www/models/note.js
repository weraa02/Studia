const mongoose = require('mongoose');
//schemat
const noteSchema = new mongoose.Schema({
    title: {
      type: String,
      //unique: true,
      required: true,
    },
    content: {
      type: String,
      //required: true,
    },
});
//model
const Note = mongoose.model('Note', noteSchema);
module.exports = Note;  //eksport modułu