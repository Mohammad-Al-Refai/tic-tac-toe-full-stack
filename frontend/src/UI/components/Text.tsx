import styled from "styled-components";
import { ThemeColors, ThemeFontScaling } from "../theme/ThemeTypes";
import { ReactNode } from "react";

const StyledText = styled.p<{
  $fontSize: ThemeFontScaling;
  $color: ThemeColors;
  $bold: boolean;
  $underline: boolean;
  $pointer: boolean;
}>`
  font-size: ${(props) => props.theme.fontSize[props.$fontSize]};
  color: ${(props) => props.theme.colors[props.$color]};
  font-weight: ${(props) => (props.$bold ? "bold" : "unset")};
  text-decoration: ${(props) => (props.$underline ? "underline" : "unset")};
  cursor: ${(props) => (props.$pointer ? "pointer" : "unset")};
`;

export function TextView({
  fontSize,
  children,
  color,
  bold = false,
  className,
  as = "p",
  underline = false,
  onClick,
  pointer = false,
}: TextViewProps) {
  return (
    <StyledText
      onClick={onClick}
      as={as}
      className={className}
      $fontSize={fontSize}
      $color={color}
      $bold={bold}
      $underline={underline}
      $pointer={pointer}
    >
      {children}
    </StyledText>
  );
}

interface TextViewProps {
  fontSize: ThemeFontScaling;
  color: ThemeColors;
  children: ReactNode;
  bold?: boolean;
  className?: string;
  as?: "span" | "p";
  underline?: boolean;
  pointer?: boolean;
  onClick?: () => void;
}
