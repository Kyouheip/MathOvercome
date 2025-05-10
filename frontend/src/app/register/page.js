// /register/page.js
"use client"
import {useState} from "react";
import {useRouter} from "next/navigation"

export default function RegisterPage(){
    const[userName,setUserName] = useState("");
    const [userId,setUserId] = useState("");
    const [password1,setPassword1] = useState("");
    const[password2,setPassword2] = useState("");
    const[error,setError] = useState("");
    const router = useRouter();

    const doRegister = async (e) => {
        e.preventDefault();
        setError("");

        const res = await fetch(
            `${process.env.NEXT_PUBLIC_API_URL}/api/auth/register`,
            {
                method: "POST",
                headers: {"Content-Type":"application/json"},
                body: JSON.stringify({userName,userId,password1,password2}),
            }
        );

        if(res.status === 201){
            router.push("/login");
        }else{
            const msg = await res.text();
            setError(msg || `エラー: ${res.status}`);
        }
    };

    return(
        <div>
            <h2>新規登録</h2>
            <form onSubmit = {doRegister}>
            <label>
                ID<br/>
                <input
                    type="text"
                    value={userId}
                    onChange={e => setUserId(e.target.value)}
                    required/>
            </label>
            <label>
                名前<br/>
                <input
                    type="text"
                    value={userName}
                    onChange={e => setUserName(e.target.value)}
                    required/>
            </label>
            <label>
                パスワード<br/>
                <input
                    type="password"
                    value={password1}
                    onChange={e => setPassword1(e.target.value)}
                    required/>
            </label>
            <label>
                パスワード確認<br/>
                <input
                    type="password"
                    value={password2}
                    onChange={e => setPassword2(e.target.value)}
                    required/>
            </label>
            {error && <p>{error}</p>}

            <button tyoe="submit">登録</button>
            </form>
        </div>
    );

}
