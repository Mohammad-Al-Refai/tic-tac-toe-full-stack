import { TextView } from "./Text";

export function Header({ name }: HeaderProps) {
  return (
    <div className="w-100 flex align-items-center j-content-evenly">
      <div></div>
      <TextView color="primary" fontSize="XL3">
        Tic Tac Toe
      </TextView>
      <TextView color="tertiary" fontSize="M">
        Name: {name}
      </TextView>
    </div>
  );
}

interface HeaderProps {
  name: string;
}
