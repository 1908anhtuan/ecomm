import axios from 'axios';
import { useState } from 'react';

export default function Register(){
const[accountName, setAccountName] = useState('');
const[password, setPassword] = useState('');

const registerFunction = (e) => {
    e.preventDefault();
    console.log('account registered');
    console.log(accountName);
    console.log(password);
    const jsonobj = {
        "accountName" : accountName,
        "password" : password
    }
    axios.post("http://localhost:8080/accounts", jsonobj)
    .then(response => {
        console.log("account id :" + response.data.accountId + "added ");
    })
    .catch(error => {
        console.log(error.toJSON());
    })
}

return(
    <form onSubmit={registerFunction}>
        <label htmlFor="text">Account</label>
        <input placeholder='account' onChange={(e) => setAccountName(e.target.value)}></input>
        <label htmlFor="text">Password</label>
        <input placeholder='password' onChange={(e) => setPassword(e.target.value)}></input>
        <button>Add account</button>

    </form>
);

}


