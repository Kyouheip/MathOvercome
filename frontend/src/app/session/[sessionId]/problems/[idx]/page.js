//session/[sessionId]/problems/[idx]/page.js
"use client"
import { useEffect, useState} from "react";
import QuestionForm from './QuestionForm';

export default function ProblemPage({params}){
    const {sessionId,idx}=params;
    const [sp, setSp] = useState(null);
    const [error, setError] = useState(null);
    const [showHint, setShowHint] = useState(false);

    useEffect(() => {
        const load = async () => {
         try{
            const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/session/${sessionId}/problems/${idx}`,
            {
            credentials: "include",
            });

            if(res.status === 401){
                setError("セッションが切れたか不正なアクセスです。ログインし直してください。");
                return ;
            }

            if (!res.ok) {
                setError(`エラーが発生しました。: ${res.status}`);
                return;
              }

            const data = await res.json();
            setSp(data);

         } catch (e){
            setError("通信エラーが発生しました。");
            return ;
         }
        };

        load();
    },[idx]);

    if (error) {
        return (
          <div>
            <p>{error}</p>
            <p><a href="/login">ログインページへ</a></p>
          </div>
        );
      }
      
    if(!sp) return <p>読み込み中...</p>;

    return(
        <div className="container mt-4">
            <h2 className="mb-3">問題 {Number(idx)+1} / {sp.total}</h2>
          <div className="bg-secondary p-3 rounded mb-4">
            <p className="mb-0 text-dark fw-bold" dangerouslySetInnerHTML={{ __html: sp.question }} />
          </div>

            <QuestionForm
                sessionId={sessionId}
                idx={Number(idx)}
                choices={sp.choices}
                initialselectedId={sp.selectedId}
                total={sp.total}
                />

             <div className="mt-4">
                <button
                 className="btn btn-outline-info"
                 onClick={() => setShowHint(!showHint)}
               >
                ヒント！
                </button>

                {showHint && (
                  <div className="mt-3 p-3 rounded bg-secondary text-dark">
                    <strong>ヒント：</strong> {sp.hint}
                  </div>
                )}
              </div>
       </div>
    );
}