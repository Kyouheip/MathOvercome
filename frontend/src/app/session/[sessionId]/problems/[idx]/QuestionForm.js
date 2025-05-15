//session/[sessionId]/problems/[idx]/QuestionForm.js
'use client'
import {useState} from 'react'
import { useRouter } from 'next/navigation'

export default function QuestionForm({sessionId,idx,choices,initialselectedId,total}){
    const [selectedId,setSelectedId] = useState(initialselectedId ?? null);
    const router = useRouter();
    const [error,setError] = useState(null);

    const submit = async (e) => {
        //formを使うときは必要。ないとリロードされReactが無視される
        e.preventDefault();

        try{
            //選択肢を送る
        const res = await fetch(
            `${process.env.NEXT_PUBLIC_API_URL}/session/${sessionId}/problems/${idx}/answer`,
            {
                method: "post",
                headers: {"Content-Type": "application/json"},
                credentials: 'include',
                body: JSON.stringify({selectedChoiceId: selectedId}),
            }
        );

        if(res.status === 401){
            setError("セッションが切れたか不正なアクセスです。ログインし直してください。");
            return ;
        }

        if(!res.ok){
            setError(`送信に失敗しました(${res.status})`);
            return ;
        }
        }catch (e) {
             setError("通信エラーが発生しました");
             return ;
        }
        const nextIdx = idx+1;
        if(nextIdx < total){
            router.push(`/session/${sessionId}/problems/${nextIdx}`)
        } else{
            router.push(`/mypage`)
        }
    };

    const handleBack = () => {
        router.back();
    }

    if (error) {
        return (
          <div>
            <p>{error}</p>
            <p><a href="/login">ログインページへ</a></p>
          </div>
        );
      }

    return(
        <form onSubmit={submit}>
          <div className="mb-3">
            {choices.map(choice => (
                <div className="form-check" key={choice.id}>
                    <input
                     className="form-check-input"
                     type="radio"
                     name="choice"
                     id={`choice-${choice.id}`}
                     checked={selectedId === choice.id}
                     onChange={() => setSelectedId(choice.id)}
                     />
                    <label htmlFor={`choice-${choice.id}`} className="form-check-label">
                      {choice.choiceText}
                    </label>
                </div>
                    )
                )
            }
           </div>

            <div className="mt-4">
                {Number(idx) > 0 && (
                    <button type="button" className="btn btn-secondary me-2" onClick={handleBack}>戻る</button>
                )}
                <button type="submit" className="btn btn-primary">次へ</button>
            </div>
         </form>
    );
}