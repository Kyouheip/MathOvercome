// /login/page.js
"use client";
import {useState} from "react";
import {useRouter} from "next/navigation"

export default function LoginPage(){
  const [userId,setUserId] = useState("");
  const [password,setPassword] = useState("");
  const [error,setError] = useState("");
  const router = useRouter();

  const doSubmit = async (e) => {
    e.preventDefault();
    setError("");

    const res = await fetch(
      `${process.env.NEXT_PUBLIC_API_URL}/api/auth/login`,
      {
      method: "POST",
      headers: {"Content-Type":"application/json"},
      credentials: "include",//セッションidを保存したCookieを受け取る
      body: JSON.stringify({userId,password}),
      }
    );

    if(res.ok){
      //ログイン成功。マイページへ
      router.push("/mypage");
    }else{
      //
      const msg = await res.text();
      setError(msg || `エラー: ${res.status}`);
    }
  }

  return(
    <div>
      <h2>ログイン</h2>
      <form onSubmit = {doSubmit}>
        <label>
          ID<br/>
          <input 
            type = "text"
            value = {userId}
            onChange = {e => setUserId(e.target.value)}
            required/>
        </label><br/>
        <label>
          パスワード<br/>
          <input
            type="password"
            value={password}
            onChange = {e => setPassword(e.target.value)}
            required/>
        </label>
        {error && <p>{error}</p>}
        <button type="submit">ログイン</button>
      </form>
      <p>新規登録は
      <a href="/register">こちら</a>
      </p>
    </div>
  );
}
