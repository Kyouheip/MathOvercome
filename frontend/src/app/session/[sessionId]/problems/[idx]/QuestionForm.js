//session/[sessionId]/problems/[idx]/QuestionForm.js
'use client'
import {useState} from 'react'
import { useRouter } from 'next/navigation'

export default function QuestionForm({sessionId,idx,sessProbId,choices,total}){
    const [selectedId,setSelectedId] = useState(null);
    const router = useRouter();

    const submit = async (e) => {
        //formを使うときは必要。ないとリロードされReactが無視される
        e.preventDefault();

        if(!selectedId){
            alert("選択肢を選んでください")
            return;
        }

        const res = await fetch(
            `${process.env.NEXT_PUBLIC_API_URL}/api/session/${sessionId}/problems/${idx}/answer`,
            {
                method: "post",
                headers: {"Content-Type": "application/json"},
                credentials: 'include',
                body: JSON.stringify({selectedChoiceId: selectedId}),
            }
        );

        if(!res.ok){
            alert("送信に失敗しました(${res.status})");
            return ;
        }

        const nextIdx = idx+1;
        if(nextIdx < total){
            router.push(`/session/${sessionId}/problems/${nextIdx}`)
        } else{
            router.push(`/mypage`)
        }
    };

    return(
        <form onSubmit={submit}>
            {choices.map(choice => (
                <label key={choice.id}>
                    <input
                     type="radio"
                     onChange={() => setSelectedId(choice.id)}
                     />
                     {choice.choiceText}
                </label>
                    )
                )
            }
            <button type="submit">次へ</button>
        </form>
    );
}