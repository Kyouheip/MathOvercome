//session/[sessionId]/problems/[idx]/page.js
"use client"
import { useEffect, useState} from "react";
import QuestionForm from './QuestionForm';

export default function ProblemPage({params}){
    const {sessionId,idx}=params;
    const [sp, setSp] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        const load = async () => {
         try{
            const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/session/${sessionId}/problems/${idx}`,
            {
            credentials: "include",
            });
            if (!res.ok) {
                setError(`エラー: ${res.status}`);
                return;
              }
            const data = await res.json();
            setSp(data);
         } catch (e){
            setError("通信エラー");
         }
        };

        load();
    },[idx]);

    if(error) return <p>{error}</p>;
    if(!sp) return <p>読み込み中...</p>;

    return(
        <div>
            <h2>問題 {Number(idx)+1}</h2>
            <p>{sp.question}</p>
            <QuestionForm
                sessionId={sessionId}
                idx={Number(idx)}
                sessProbId={sp.id}
                choices={sp.choices}
                total={sp.total}
                />
        </div>
    )
}