"use client";
import { useEffect, useState } from "react";

export default function TestPage() {
  const [problems, setProblems] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    const load = async () => {
      try {
        const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/session/st`, {
          credentials: "include",
        });

        if (res.status === 401) {
          setError("ログインしていません");
          return;
        }

        if (!res.ok) {
          const text = await res.text();
          setError(`エラー発生: ${res.status} - ${text}`);
          return;
        }

        const data = await res.json();
        if (!Array.isArray(data)) {
          setError("返ってきたデータが配列ではありません");
          return;
        }

        setProblems(data);
      } catch (err) {
        setError("通信エラー: " + err.message);
      }
    };

    load();
  }, []);

  if (error) return <p>{error}</p>;

  return (
    <div>
      {problems.map((problem) => (
        <div key={problem.id}>
          <p>id: {problem.id}</p>
        <p dangerouslySetInnerHTML={{ __html: problem.question }}/>

          <p>hint: {problem.hint}</p>
          <ul>
            {problem.choices.map((choice) => (
              <li key={choice.id}>{choice.choiceText}</li>
            ))}
          </ul>
          <hr />
        </div>
      ))}
    </div>
  );
}
