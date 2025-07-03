const jwt = require("jsonwebtoken");

const config = process.env; //

const verifyToken = (req, res, next) => {
    //const token = req.body.token || req.query.token || req.headers["x-access-token"];
    //const token = document.cookie.replace(/(?:(?:^|.*;\s*)token\s*=\s*([^;]*).*$)|^.*$/, "$1");
    const token = req.cookies.token;

    if (!token) {
        console.log('verifu - nie ma tokenu', token)
        return res.redirect("/login"); // Redirect to the login page if no token is provided
        //return res.status(403).send("A token is required for authentication");
    }
    try {
        const decoded = jwt.verify(token, config.TOKEN_KEY);
        req.user = decoded;
        console.log('verifu - koniec try');
        next();     //
    } catch (err) {
        return res.status(401).send("Invalid Token");
    }
    //return next();
};


module.exports = verifyToken;